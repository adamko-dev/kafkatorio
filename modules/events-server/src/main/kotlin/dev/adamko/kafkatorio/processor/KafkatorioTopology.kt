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
import dev.adamko.kafkatorio.events.schema.converters.toHexadecimal
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.aggregate
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.groupBy
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.merge
import dev.adamko.kotka.extensions.streams.reduce
import dev.adamko.kotka.extensions.streams.to
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.mapValues
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
import kotlinx.serialization.Transient
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

//  private lateinit var tilePrototypesStore: ReadOnlyKeyValueStore<String, ValueAndTimestamp<MapTilePrototype>>

  fun build() {

    parseAndSplitServerLog()
    sendPlayerUpdatesToWebSocket()
//    createMapTileImages()
    val tilePrototypesTable: KTable<SurfaceIndex, SurfacePrototypesData> = tilePrototypesTable()

    val tilesTable: KTable<TileUpdateRecordKey, TileUpdateRecord> = buildTilesTable()

    val tilesGroupedByChunk: KTable<MapChunkDataPosition, MapChunkData> =
      buildChunkedTilesTable(tilesTable)

    createChunkTileImage(tilePrototypesTable, tilesGroupedByChunk)

    val topology = builder.build()

    val streams = KafkaStreams(topology, appProps.kafkaConfig)

//    streams.cleanUp()
    streams.setUncaughtExceptionHandler(StreamsExceptionHandler())
    Runtime.getRuntime().addShutdownHook(Thread { streams.close(Duration.ofSeconds(1)) })

    val description: TopologyDescription = topology.describe()
    println(description)

    streams.start()

//    tilePrototypesStore = streams.store(
//      StoreQueryParameters.fromNameAndType(
//        tilePrototypesTable.queryableStoreName(),
//        QueryableStoreTypes.timestampedKeyValueStore<String, MapTilePrototype>()
//      )
//    )
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
          is FactorioEvent ->
            "kafkatorio.${value.packetType.name}.${value.data.objectName.name}"
          is FactorioConfigurationUpdate ->
            "kafkatorio.${value.packetType.name}.FactorioConfigurationUpdate"
          is FactorioPrototypes ->
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

  @Serializable
  data class SurfaceIndex(
    val surfaceIndex: Int
  ) {
    override fun toString() = "$surfaceIndex"
  }

  @Serializable
  data class SurfacePrototypesData(
    val surfaceIndex: SurfaceIndex,
    val mapTilePrototypes: Set<MapTilePrototype>,
  )

  private fun tilePrototypesTable(): KTable<SurfaceIndex, SurfacePrototypesData> {

//    val tilePrototypesStreamName = "kafkatorio.stream.prototypes-tiles"
//    val tilePrototypesStoreName = "kafkatorio.store.prototypes-tiles"

    return builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.PROTOTYPES}.all",
      consumedAs(
        "consume-all-prototypes-packets",
        Serdes.String(),
        jsonMapper.serde<FactorioPrototypes>()
      )
    )
      .map("flatten-map-tile-prototypes") { _, prototypes: FactorioPrototypes ->
        val tilePrototypes = prototypes.prototypes
          .filterIsInstance<MapTilePrototype>()
          .toSet()
        val key = SurfaceIndex(1)   // TODO make surfaceIndex come from Factorio, not hard coded
        val data = SurfacePrototypesData(key, tilePrototypes)
        key to data
      }
      .peek { _, value ->
        println("tile prototypes: ${value.mapTilePrototypes.joinToString { it.name }}")
      }
      .groupByKey(
        groupedAs(
          "grouping-tile-prototypes-by-surface-index",
          jsonMapper.serde(),
          jsonMapper.serde()
        )
      )
      .reduce(
        "group-surface-prototypes",
        materializedWith(
//          "kafkatorio.surface-prototypes",
          jsonMapper.serde(),
          jsonMapper.serde()
        )
      ) { value1: SurfacePrototypesData, value2: SurfacePrototypesData ->
        value1.copy(mapTilePrototypes = value1.mapTilePrototypes + value2.mapTilePrototypes)
      }
//      .toTable(
//        materializedAs(
//          "kafkatorio.prototypes.map-tiles",
//          jsonMapper.serde(),
//          jsonMapper.serde()
//        )
//      )
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
  data class TileUpdateRecordKey(
    val surfaceIndex: SurfaceIndex,
    val tilePosition: MapTilePosition,
  )

  @Serializable
  data class TileUpdateRecord(
    val surfaceIndex: SurfaceIndex,
    val tilePosition: MapTilePosition,
    val tile: MapTile,
  ) : TopicRecord<TileUpdateRecordKey> {
    @Transient
    override val topicKey: TileUpdateRecordKey = TileUpdateRecordKey(surfaceIndex, tilePosition)
  }

  private fun buildTilesTable(): KTable<TileUpdateRecordKey, TileUpdateRecord> {

    val luaTilesUpdatesStream: KStream<String, MapTiles> = builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
      consumedAs("consume-map-tiles-packets", Serdes.String(), jsonMapper.serde<FactorioEvent>())
    )
      .filter("events.filter.map-tiles") { _: String, event: FactorioEvent -> event.data is MapTiles }
      .mapValues("events.extract-map-tiles") { _, event: FactorioEvent -> (event.data as? MapTiles)!! }

    val chunkTilesUpdateStream: KStream<String, MapTiles> = builder.stream(
      "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.MapChunk}",
      consumedAs("consume-map-chunk-packets", Serdes.String(), jsonMapper.serde<FactorioEvent>())
    )
      .filter("events.filter.map-chunks") { _: String, event: FactorioEvent -> event.data is MapChunk }
      .mapValues("events.extract-map-chunks") { _, event: FactorioEvent -> (event.data as? MapChunk)!!.tiles }

    return luaTilesUpdatesStream
      .merge("luaTilesUpdatesStream-and-chunkTilesUpdateStream", chunkTilesUpdateStream)
      .flatMapTopicRecords("all-tiles-convert-to-TileUpdateRecord") { _, mapTiles: MapTiles ->
//          println("all map tiles update ${mapTiles.tiles.size}")
        mapTiles.tiles.map { tile ->
          TileUpdateRecord(
            SurfaceIndex(mapTiles.surfaceIndex),
            tile.position,
            tile,
          )
        }
      }
      .toTable(materializedWith(jsonMapper.serde(), jsonMapper.serde()))
//      .suppress(
//        Suppressed.untilTimeLimit(
//          Duration.ofSeconds(30),
//          Suppressed.BufferConfig
//            .maxRecords(100)
//            .emitEarlyWhenFull()
//        )
//      )
  }

  @Serializable
  data class MapChunkDataPosition(
    val position: MapChunkPosition,
    val surfaceIndex: SurfaceIndex,
  )

  @Serializable
  data class MapChunkData(
    val chunkPosition: MapChunkDataPosition,
    val tiles: Set<MapTile>,
  )

  private fun buildChunkedTilesTable(
    tilesTable: KTable<TileUpdateRecordKey, TileUpdateRecord>
  ): KTable<MapChunkDataPosition, MapChunkData> {

    @Serializable
    data class MapChunkDataAggregator(
      val tiles: MutableSet<MapTile> = mutableSetOf(),
    )

    return tilesTable
      .toStream()
//      .repartition(
//        repartitionedAs(
//          "repartition-all-tiles-before-chunk-grouping",
//          jsonMapper.serde(),
//          jsonMapper.serde()
//        )
//      )
      .groupBy<TileUpdateRecordKey, TileUpdateRecord, MapChunkDataPosition>(
        groupedAs("grouping-tiles-by-chunk-pos", jsonMapper.serde(), jsonMapper.serde()),
      ) { key: TileUpdateRecordKey, value: TileUpdateRecord ->
        MapChunkDataPosition(
          value.tilePosition.toMapChunkPosition(),
          key.surfaceIndex
        )
      }
      .aggregate<MapChunkDataPosition, TileUpdateRecord, MapChunkDataAggregator>(
        "reducing-tiles-grouped-by-chunk-position",
        materializedWith(jsonMapper.serde(), jsonMapper.serde()),
        { MapChunkDataAggregator() },
      ) { _: MapChunkDataPosition, value: TileUpdateRecord, aggregate: MapChunkDataAggregator ->
        aggregate.tiles.add(value.tile)
        aggregate
      }
      // TODO maybe set serdes here?
      .mapValues(
        "tiles-grouped-by-chunk-position-finalise-aggregation",
        materializedWith(jsonMapper.serde(), jsonMapper.serde()),
      ) { k, v ->
        MapChunkData(k, v.tiles)
      }

  }

  private fun createChunkTileImage(
    tilePrototypesTable: KTable<SurfaceIndex, SurfacePrototypesData>,
    tilesGroupedByChunk: KTable<MapChunkDataPosition, MapChunkData>,
  ) {

    @Serializable
    data class ChunkTilesAndProtos(
      val chunkTiles: MapChunkData,
      val protos: SurfacePrototypesData
    )

//    tilesGroupedByChunk
//      .join<ChunkTilesAndProtos, SurfaceIndex, SurfacePrototypesData>(
//        tilePrototypesTable,
//        { cd: MapChunkData -> cd.chunkPosition.surfaceIndex },
//        { chunkTiles: MapChunkData, protos: SurfacePrototypesData ->
//          ChunkTilesAndProtos(chunkTiles, protos)
//        },
//        namedAs("joining-chunks-tiles-prototypes"),
//        materializedAs(
//          "joined-chunked-tiles-with-prototypes",
//          jsonMapper.serde(),
//          jsonMapper.serde()
//        ),
//      )
    tilesGroupedByChunk
      .join<MapChunkDataPosition, MapChunkData, SurfaceIndex, SurfacePrototypesData, ChunkTilesAndProtos>(
        tilePrototypesTable,
        "enrich-tiles-with-prototype-colour",
        materializedAs(
          "joined-chunked-tiles-with-prototypes",
          jsonMapper.serde(),
          jsonMapper.serde()
        ),
        { it.chunkPosition.surfaceIndex },
      ) { chunkTiles: MapChunkData, protos: SurfacePrototypesData ->
        ChunkTilesAndProtos(chunkTiles, protos)
      }
      .suppress(
        Suppressed.untilTimeLimit(
          Duration.ofSeconds(30),
          Suppressed.BufferConfig.maxRecords(30)
        )
      )
      .toStream()
      .foreach { _, (chunkTiles: MapChunkData, protos: SurfacePrototypesData) ->
        runCatching {
          saveMapTilesPng(chunkTiles, protos)
        }.onFailure { e ->
          println("error saving map tile png chunk:${chunkTiles.chunkPosition}")
          e.printStackTrace()
          throw e
        }
      }
  }


  private fun saveMapTilesPng(chunkTiles: MapChunkData, protos: SurfacePrototypesData) {

    val chunkPos = chunkTiles.chunkPosition

    val tilePrototypes: Map<String, MapTilePrototype?> =
      protos.mapTilePrototypes.associateBy { it.name }.withDefault { null }

    val chunkOriginX = chunkTiles.chunkPosition.position.leftTopTile.x
//    val chunkOriginY = chunk.chunkPosition.position.rightBottomTile.y
    val chunkOriginY = chunkTiles.chunkPosition.position.leftTopTile.y

    val chunkImage =
      ImmutableImage.filled(
        MAP_CHUNK_SIZE,
        MAP_CHUNK_SIZE,
        Color.BLACK,
        BufferedImage.TYPE_INT_ARGB
      )

    chunkTiles.tiles.forEach { tile ->

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
      File(
        "src/main/resources/kafkatorio-web-map/s${chunkPos.surfaceIndex}/z$zoom/x${chunkOriginX}/y${chunkOriginY}.png"
      )

    if (file.parentFile.mkdirs()) {
      println("created new map tile parentFile directory ${file.absolutePath}")
    }

    println("saving map tile $file")

    chunkImage.output(PngWriter.MaxCompression, file)
  }

}
