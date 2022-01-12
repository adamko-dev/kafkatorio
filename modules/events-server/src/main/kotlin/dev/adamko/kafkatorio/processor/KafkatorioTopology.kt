package dev.adamko.kafkatorio.processor

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.pixels.Pixel
import com.sksamuel.scrimage.pixels.PixelTools
import dev.adamko.kafkatorio.events.schema.FactorioConfigurationUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.extensions.kafkastreams.KafkaStreamsExtensions.materializedAs
import dev.adamko.kafkatorio.extensions.kafkastreams.KeyPairExtensions.toKeyValue
import dev.adamko.kafkatorio.extensions.kafkastreams.KotlinxSerializationExtensions.serde
import dev.adamko.kafkatorio.extensions.kafkastreams.TopicData
import dev.adamko.kafkatorio.extensions.kafkastreams.TopicData.Companion.toKeyValue
import java.awt.image.BufferedImage
import java.io.File
import java.time.Duration
import kotlin.math.roundToInt
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StoreQueryParameters
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.TopologyDescription
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.apache.kafka.streams.state.ValueAndTimestamp


class KafkatorioTopology(
  private val websocketServer: WebsocketServer,
  private val builder: StreamsBuilder = StreamsBuilder(),
) {

  private val sourceTopic = "factorio-server-log"

  private lateinit var tilePrototypesStore: ReadOnlyKeyValueStore<String, ValueAndTimestamp<MapTilePrototype>>
  fun build() {

    parseAndSplitServerLog()
    sendPlayerUpdatesToWebSocket()
//    createMapTileImages()
    val tilePrototypesTable: KTable<String, MapTilePrototype> = tilePrototypesTable()

    val topology = builder.build()

    val streams = KafkaStreams(topology, appProps.kafkaConfig)

    tilePrototypesStore = streams.store(
      StoreQueryParameters.fromNameAndType(
        tilePrototypesTable.queryableStoreName(),
        QueryableStoreTypes.timestampedKeyValueStore<String, MapTilePrototype>()
      )
    )

    streams.setUncaughtExceptionHandler(StreamsExceptionHandler())

    Runtime.getRuntime().addShutdownHook(Thread { streams.close(Duration.ofSeconds(1)) })

//  streams.cleanUp()

    val description: TopologyDescription = topology.describe()
    println(description)

    streams.start()
  }

  private fun parseAndSplitServerLog() {

    builder.stream(
      sourceTopic,
      Consumed.with(Serdes.String(), Serdes.String())
    )
      .mapValues { readOnlyKey, value ->
        println("Mapping $readOnlyKey:$value")
        jsonMapper.decodeFromString<KafkatorioPacket>(value)
      }
      .to(
        { _, value, _ ->
//        println("[$key] sending event:${value.eventType} to topic:${value.data.objectName()}")
          when (value) {
            is FactorioEvent               ->
              "kafkatorio.${value.packetType.name}.${value.data.objectName.name}"
            is FactorioConfigurationUpdate ->
              "kafkatorio.${value.packetType.name}.FactorioConfigurationUpdate"
          }
        },
        Produced.with(Serdes.String(), KafkatorioPacketSerde)
      )
  }

  private fun sendPlayerUpdatesToWebSocket() {
    builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaPlayer}",
      Consumed.with(Serdes.String(), KafkatorioPacketSerde)
    )
      .foreach { _, value ->
        websocketServer.sendMessage(jsonMapper.encodeToString(value))
      }
  }

  private fun tilePrototypesTable(): KTable<String, MapTilePrototype> {
    return builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.CONFIG}.FactorioConfigurationUpdate",
      Consumed.with(Serdes.String(), jsonMapper.serde<FactorioConfigurationUpdate>())
    )
      .flatMap { _, update: FactorioConfigurationUpdate ->
        update
          .prototypes
          .filterIsInstance<MapTilePrototype>()
          .map { proto -> proto.name to proto }
          .map { it.toKeyValue() }
      }
      .toTable(
        materializedAs(
          "kafkatorio.prototypes.map-tiles",
          jsonMapper.serde(),
          jsonMapper.serde()
        )
      )
  }
  
  private fun mapChunksStream() {

    val luaTilesUpdatesStream: KStream<String, MapTiles> = builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.MapChunk}",
      Consumed.with(Serdes.String(), jsonMapper.serde<FactorioEvent>())
    )
      .filter { _, event: FactorioEvent -> event.data is MapTiles }
      .mapValues { _, event: FactorioEvent -> (event.data as? MapTiles)!! }

    val chunkTilesUpdateStream: KStream<String, MapTiles> = builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
      Consumed.with(Serdes.String(), jsonMapper.serde<FactorioEvent>())
    )
      .filter { _, event: FactorioEvent -> event.data is MapChunk }
      .mapValues { _, event: FactorioEvent -> (event.data as? MapChunk)!!.tiles }

    @Serializable
    data class TileUpdateRecordKey(
      val surfaceIndex: Int,
      val tilePosition: MapTilePosition,
    )

    @Serializable
    data class TileUpdateRecord(
      val surfaceIndex: Int,
      val tilePosition: MapTilePosition,
      val tile: MapTile
    ) : TopicData<TileUpdateRecordKey> {
      override val topicKey: TileUpdateRecordKey =
        TileUpdateRecordKey(surfaceIndex, tilePosition)
    }

    val allTileUpdatesStream: KStream<TileUpdateRecordKey, TileUpdateRecord> =
      luaTilesUpdatesStream
        .merge(chunkTilesUpdateStream)
        .flatMap { _, mapTiles: MapTiles ->
          mapTiles.tiles.map { tile ->
            TileUpdateRecord(
              mapTiles.surfaceIndex,
              tile.position,
              tile
            ).toKeyValue()
          }
        }

    @Serializable
    data class MapChunkDataPosition(
      val position: MapChunkPosition,
      val surfaceIndex: Int,
    )

    @Serializable
    data class MapChunkData(
      val position: MapChunkPosition,
      val surfaceIndex: Int,
      val tiles: Set<MapTile>,
    ) : TopicData<MapChunkDataPosition> {
      override val topicKey = MapChunkDataPosition(position, surfaceIndex)

      constructor(key: MapChunkDataPosition, tiles: Set<MapTile>) :
          this(key.position, key.surfaceIndex, tiles)
    }

    val chunksTable =
      allTileUpdatesStream
        .groupBy { _: TileUpdateRecordKey, value: TileUpdateRecord ->
          MapChunkDataPosition(
            value.tilePosition.toMapChunkPosition(),
            value.surfaceIndex,
          )
        }
        .aggregate(
          { setOf() },
        ) { _: MapChunkDataPosition, v: TileUpdateRecord, tiles: Set<MapTile> ->
          tiles + v.tile
        }
        .mapValues { chunkPosition: MapChunkDataPosition, tiles: Set<MapTile> ->
          MapChunkData(chunkPosition, tiles)
        }

    chunksTable.toStream()
      .foreach { key: MapChunkDataPosition, value: MapChunkData ->


        value.tiles
          .map { tile ->

            val colour = tilePrototypesStore.get(tile.prototypeName)
              .value()
              .mapColour
              .asDecimal()


            val pixelColour = PixelTools.argb(
              colour.alpha.roundToInt(),
              colour.red.roundToInt(),
              colour.green.roundToInt(),
              colour.blue.roundToInt(),
            )

            Pixel(
              tile.position.x,
              tile.position.y,
              pixelColour
            )
          }

        val chunkImg = ImmutableImage.create(32, 32, BufferedImage.TYPE_INT_ARGB)

        val zoom = 1u
        chunkImg.output(
          PngWriter.MaxCompression,
          File("${key.surfaceIndex}/$zoom/${key.position.x}/${key.position.y}.png")
        )
      }
  }

//
//  private fun createMapTileImages() {
//
//    val mapTilesByPrototypeName: KStream<String, MapTile> =
//      builder.stream(
//        "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
//        Consumed.with(Serdes.String(), KafkatorioPacketSerde)
//      )
//        .filter { _, packet -> packet is FactorioEvent && packet.data is MapTiles }
//        .mapValues { packet -> ((packet as? FactorioEvent)?.data as? MapTiles)!! }
//        .flatMap { _, mapTiles ->
//          mapTiles
//            .tiles
//            .map { tile -> tile.prototypeName to tile }
//            .map { it.toKeyValue() }
//        }
////        .groupByKey()
//
//    @Serializable
//    data class TileSurfaceCoord(
//      val surfaceIndex: Int,
//      val position: MapTilePosition,
//    )
//
//    @Serializable
//    data class TileSurfaceState(
//      val surfaceIndex: Int,
//      val position: MapTilePosition,
//      val prototypeName: String,
//    )
//
//    val mapTilesTable: KTable<TileSurfaceCoord, TileSurfaceState> =
//      builder.stream(
//        "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
//        Consumed.with(Serdes.String(), KafkatorioPacketSerde)
//      )
//        .filter { _, packet -> packet is FactorioEvent && packet.data is MapTiles }
//        .mapValues { packet -> ((packet as? FactorioEvent)?.data as? MapTiles)!! }
//        .flatMap({ _, mapTiles: MapTiles ->
//          mapTiles.tiles.map { tile: MapTile ->
//            val key = TileSurfaceCoord(mapTiles.surfaceIndex, tile.position)
//            val value = TileSurfaceState(mapTiles.surfaceIndex, tile.position, tile.prototypeName)
//            (key to value).toKeyValue()
//          }
//        }
//        )
//        .toTable(
//          materializedAs(
//            "kafkatorio.table.map-tile-status",
//            jsonMapper.serde(),
//            jsonMapper.serde(),
//          )
//        )
//
//
//    val tilePrototypesTable: KTable<String, MapTilePrototype> =
//      builder.stream(
//        "kafkatorio.${KafkatorioPacket.PacketType.CONFIG}.FactorioConfigurationUpdate",
//        Consumed.with(Serdes.String(), KafkatorioPacketSerde)
//      )
//        .flatMap(KeyValueMapper<String, KafkatorioPacket, List<KeyValue<String, MapTilePrototype>>> { _, packet ->
//          when (packet) {
//            is FactorioConfigurationUpdate ->
//              packet.prototypes
//                .filterIsInstance<MapTilePrototype>()
//                .map { proto -> proto.name to proto }
//                .map { it.toKeyValue() }
//            is FactorioEvent               ->
//              emptyList()
//          }
//        })
//        .toTable(
//          materializedAs("kafkatorio.table.tile-prototypes", Serdes.String(), jsonMapper.serde())
//        )
//
//
//    @Serializable
//    data class TileSurfaceData(
//      val surfaceIndex: Int,
//      val position: MapTilePosition,
//      val prototype: MapTilePrototype,
//    )
//
//    val enrichedTiles: KTable<TileSurfaceCoord, TileSurfaceData> =
//      mapTilesTable.join<TileSurfaceData, String, MapTilePrototype>(
//        tilePrototypesTable,
//        { tile: TileSurfaceState ->
//          tile.prototypeName
//        },
//        { tile: TileSurfaceState, proto: MapTilePrototype ->
//          TileSurfaceData(tile.surfaceIndex, tile.position, proto)
//        },
//      )
//
//  }

}
