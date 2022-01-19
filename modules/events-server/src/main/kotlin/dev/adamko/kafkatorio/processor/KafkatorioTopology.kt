package dev.adamko.kafkatorio.processor

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import dev.adamko.kafkatorio.events.schema.FactorioConfigurationUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MAP_CHUNK_SIZE
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.events.schema.converters.leftTopTile
import dev.adamko.kafkatorio.events.schema.converters.rightBottomTile
import dev.adamko.kafkatorio.events.schema.converters.toHexadecimal
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.merge
import dev.adamko.kotka.extensions.streams.to
import dev.adamko.kotka.extensions.streams.toTable
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.kxs.serde
import dev.adamko.kotka.topicdata.TopicRecord
import dev.adamko.kotka.topicdata.flatMapTopicRecords
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.time.Duration
import kotlin.math.abs
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
import org.apache.kafka.streams.kstream.Suppressed
import org.apache.kafka.streams.processor.RecordContext
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.apache.kafka.streams.state.ValueAndTimestamp


class KafkatorioTopology(
  private val websocketServer: WebsocketServer,
  private val builder: StreamsBuilder = StreamsBuilder(),
) {

  companion object {
    const val sourceTopic = "factorio-server-log"
  }

  private lateinit var tilePrototypesStore: ReadOnlyKeyValueStore<String, ValueAndTimestamp<MapTilePrototype>>

  fun build() {

    parseAndSplitServerLog()
    sendPlayerUpdatesToWebSocket()
//    createMapTileImages()
    val tilePrototypesTable: KTable<String, MapTilePrototype> = tilePrototypesTable()
    mapChunksStream(tilePrototypesTable)

    val topology = builder.build()

    val streams = KafkaStreams(topology, appProps.kafkaConfig)
//    streams.cleanUp()

    streams.setUncaughtExceptionHandler(StreamsExceptionHandler())

    Runtime.getRuntime().addShutdownHook(Thread { streams.close(Duration.ofSeconds(10)) })


    val description: TopologyDescription = topology.describe()
    println(description)

    streams.start()

    tilePrototypesStore = streams.store(
      StoreQueryParameters.fromNameAndType(
        tilePrototypesTable.queryableStoreName(),
        QueryableStoreTypes.timestampedKeyValueStore<String, MapTilePrototype>()
      )
    )
  }

  private fun parseAndSplitServerLog() {

    builder.stream(
      sourceTopic,
      consumedAs(
        "read-raw-packets-from-server",
        Serdes.String(),
        Serdes.String(),
      )
    )
      .mapValues("decode-packets") { _: String, value: String ->
//        println("Mapping $readOnlyKey:$value")
        jsonMapper.decodeFromString<KafkatorioPacket>(value)
      }
      .to(
        producedAs(
          "split-server-log",
          Serdes.String(),
          KafkatorioPacketSerde
        )
      ) { _: String, value: KafkatorioPacket, _: RecordContext ->
//        println("[$key] sending event:${value.eventType} to topic:${value.data.objectName()}")
        when (value) {
          is FactorioEvent               ->
            "kafkatorio.${value.packetType.name}.${value.data.objectName.name}"
          is FactorioConfigurationUpdate ->
            "kafkatorio.${value.packetType.name}.FactorioConfigurationUpdate"
          is FactorioPrototypes          ->
            "kafkatorio.${value.packetType.name}.all"
        }
      }

  }

  private fun sendPlayerUpdatesToWebSocket() {
    builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaPlayer}",
      Consumed.with(Serdes.String(), KafkatorioPacketSerde)
    )
      .foreach { _, value ->
//        println("sending ${value.packetType} packet to websocket")
        websocketServer.sendMessage(jsonMapper.encodeToString(value))
      }
  }

  private fun tilePrototypesTable(): KTable<String, MapTilePrototype> {

//    val tilePrototypesStreamName = "kafkatorio.stream.prototypes-tiles"
//    val tilePrototypesStoreName = "kafkatorio.store.prototypes-tiles"

    return builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.PROTOTYPES}.all",
      Consumed.with(Serdes.String(), jsonMapper.serde<FactorioPrototypes>())
    )
      .flatMap("flatten-map-tile-prototypes") { _, prototypes: FactorioPrototypes ->
        prototypes.prototypes
          .filterIsInstance<MapTilePrototype>()
          .map { proto -> proto.name to proto }
      }
      .peek { _, value ->
        println("tile prototype: ${value.name}")
      }
      .toTable(
        materializedAs(
          "kafkatorio.prototypes.map-tiles",
          jsonMapper.serde(),
          jsonMapper.serde()
        )
      )
//      .to(
//        tilePrototypesStreamName,
//      )

//    return builder.globalTable(
//      tilePrototypesStreamName,
//      consumedAs(
//        "send-tile-protos-to-gkt-store",
//        jsonMapper.serde(),
//        jsonMapper.serde()
//      ),
//      materializedAs(
//        storeName = tilePrototypesStoreName,
//        keySerde = jsonMapper.serde(),
//        valueSerde = jsonMapper.serde(),
//      )
//    )
  }

  @Serializable
  data class MapTileWithPrototype(
    val position: MapTilePosition,
    val prototype: MapTilePrototype,
  )

  @Serializable
  data class MapChunkDataPosition(
    val position: MapChunkPosition,
    val surfaceIndex: Int,
  )

  @Serializable
  data class MapChunkData(
    val chunkPosition: MapChunkDataPosition,
    val tiles: Set<MapTile>,
    val tilePrototypes: Set<MapTilePrototype>,
  )

  private fun mapChunksStream(tilePrototypesTable: KTable<String, MapTilePrototype>) {

    val luaTilesUpdatesStream: KStream<String, MapTiles> = builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
      Consumed.with(Serdes.String(), jsonMapper.serde<FactorioEvent>())
    )
//      .peek { k, v ->
//        println("trying to handle MapChunk update... $k: ${v.eventType}")
//      }
      .filter("events.filter.map-tiles") { _: String, event: FactorioEvent -> event.data is MapTiles }
      .mapValues("events.extract-map-tiles") { _, event: FactorioEvent -> (event.data as? MapTiles)!! }
//      .peek { key, value ->
//        println("MapChunk update $key, tiles count: ${value.tiles.size}")
//      }


    val chunkTilesUpdateStream: KStream<String, MapTiles> = builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.MapChunk}",
      Consumed.with(Serdes.String(), jsonMapper.serde<FactorioEvent>())
    )
      .peek { k, v ->
        println("trying to handle LuaTiles update... $k: ${v.eventType} / ${v.data is MapChunk}")
      }
      .filter("events.filter.map-chunks") { _: String, event: FactorioEvent -> event.data is MapChunk }
      .mapValues("events.extract-map-chunks") { _, event: FactorioEvent -> (event.data as? MapChunk)!!.tiles }
      .peek { key, value ->
        println("LuaTiles update $key, tiles count: ${value.tiles.size}")
      }

    @Serializable
    data class TileUpdateRecordKey(
      val surfaceIndex: Int,
      val tilePosition: MapTilePosition,
    )

    @Serializable
    data class TileUpdateRecord(
      val surfaceIndex: Int,
      val tilePosition: MapTilePosition,
      val tile: MapTile,
      val tilePrototype: MapTilePrototype?,
    ) : TopicRecord<TileUpdateRecordKey> {
      override val topicKey: TileUpdateRecordKey = TileUpdateRecordKey(surfaceIndex, tilePosition)
    }

    val allTileUpdatesStream: KStream<TileUpdateRecordKey, TileUpdateRecord> =
      luaTilesUpdatesStream
        .merge("luaTilesUpdatesStream-and-chunkTilesUpdateStream", chunkTilesUpdateStream)
        .flatMapTopicRecords("all-tiles-convert-to-TileUpdateRecord") { _, mapTiles: MapTiles ->
//          println("all map tiles update ${mapTiles.tiles.size}")
          mapTiles.tiles.map { tile ->
            TileUpdateRecord(
              mapTiles.surfaceIndex,
              tile.position,
              tile,
              null
            )
          }
        }
        .toTable(
          "all-tile-updates-table",
          materializedWith(jsonMapper.serde(), jsonMapper.serde())
        )
        .join(
          tilePrototypesTable,
          "enrich-tiles-with-prototypes",
          materializedWith(jsonMapper.serde(), jsonMapper.serde()),
          { it.tile.prototypeName },
        ) { v1: TileUpdateRecord, v2: MapTilePrototype ->
          v1.copy(tilePrototype = v2)
        }
        .toStream()
        .filter { _, value ->
          value.tilePrototype != null
        }

    val chunksTable: KTable<MapChunkDataPosition, MapChunkData> =
      allTileUpdatesStream
        .map("map-tile-updates-to-MapChunkData") { _: TileUpdateRecordKey, value: TileUpdateRecord ->
          val key = MapChunkDataPosition(
            value.tilePosition.toMapChunkPosition(),
            value.surfaceIndex,
          )
          key to MapChunkData(
            key,
            setOf(value.tile),
            setOfNotNull(value.tilePrototype)
          )
        }
        // group all tiles by chunk position
        .groupByKey(
          groupedAs("group-tile-updates-per-chunk-position", jsonMapper.serde(), jsonMapper.serde())
        )
        .reduce { tileA, tileB ->
          tileA.copy(
            tiles = tileA.tiles + tileB.tiles,
            tilePrototypes = tileA.tilePrototypes + tileB.tilePrototypes
          )
        }
        .suppress(
          Suppressed.untilTimeLimit(
            Duration.ofSeconds(30),
            Suppressed.BufferConfig.maxRecords(32*32*2)
          )
        )

    chunksTable
      .toStream()
      .foreach { key: MapChunkDataPosition, value: MapChunkData ->
        runCatching {
          saveMapTilesPng(key, value)
        }.onFailure { e ->
          println("error saving map tile png chunk:${value.chunkPosition}")
          e.printStackTrace()
          throw e
        }
      }
  }


  private fun saveMapTilesPng(key: MapChunkDataPosition, chunk: MapChunkData) {

    val tilePrototypes: Map<String, MapTilePrototype?> =
      chunk.tilePrototypes.associateBy { it.name }.withDefault { null }

    val chunkOriginX = chunk.chunkPosition.position.leftTopTile.x
//    val chunkOriginY = chunk.chunkPosition.position.rightBottomTile.y
    val chunkOriginY = chunk.chunkPosition.position.leftTopTile.y

    val chunkImage =
      ImmutableImage.filled(
        MAP_CHUNK_SIZE,
        MAP_CHUNK_SIZE,
        Color.BLACK,
        BufferedImage.TYPE_INT_ARGB
      )

    chunk.tiles.forEach { tile ->

      val prototypeColour = tilePrototypes
        .getValue(tile.prototypeName)
        ?.mapColour
        ?.toHexadecimal()

      val rgbColour = when (prototypeColour) {
        null -> {
          println("missing prototype: ${tile.prototypeName}")
          RGBColor(11, 11, 11, 0)
        }
        else -> RGBColor(
          prototypeColour.red.roundToInt(),
          prototypeColour.green.roundToInt(),
          prototypeColour.blue.roundToInt(),
          prototypeColour.alpha.roundToInt(),
        )
      }

      val pixelX = abs(abs(tile.position.x) - abs(chunkOriginX))
      val pixelY = abs(abs(tile.position.y) - abs(chunkOriginY))

      chunkImage.setColor(
        pixelX,
        pixelY,
        rgbColour
      )

    }

    val zoom = 1u
    val file =
      File("src/main/resources/kafkatorio-web-map/s${key.surfaceIndex}/z$zoom/x${key.position.x}/y${key.position.y}.png")

    if (file.parentFile.mkdirs()) {
      println("created new map tile parentFile directory ${file.absolutePath}")
    }

    println("saving map tile $file")

    chunkImage.output(PngWriter.MaxCompression, file)
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
