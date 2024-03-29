package dev.adamko.kafkatorio.processor.misc

import dev.adamko.kafkatorio.library.kxsBinary
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.server.processor.topology.ServerMapChunkTiles
import java.util.UUID
import kotlinx.serialization.encodeToByteArray
import org.junit.jupiter.api.Test


class Blah {

  @Test
  fun asdad() {

    val tiles = ServerMapChunkTiles<ColourHex>(
      chunkId = ServerMapChunkId(
        serverId = FactorioServerId(UUID.randomUUID().toString()),
        layer = ServerMapTileLayer.BUILDING,
        chunkPosition = MapChunkPosition(Int.MAX_VALUE, Int.MAX_VALUE, ChunkSize.MAX),
        surfaceIndex = SurfaceIndex(UInt.MAX_VALUE),
      ),
      buildMap {
        repeat(512) { x ->
          repeat(512) { y ->
            put(MapTilePosition(x, y), ColourHex(255u, 255u, 255u, 255u))
          }
        }
      }
    )

    val encode = kxsBinary.encodeToByteArray(tiles)

    encode.inputStream()
    val size = encode.size * Byte.SIZE_BYTES

    println(size)


    val map2 = buildList {
      repeat(512) { x ->
        repeat(512) { y ->
          val element = (x to y) to Int.MAX_VALUE
          add(element)
        }
      }
    }
//      .toTypedArray()
    val encode2 = kxsBinary.encodeToByteArray(map2)

    encode2.inputStream()
    val size2 = encode2.size * Byte.SIZE_BYTES

    println(size2)

  }
}
