package dev.adamko.kafkatorio.processor.misc

import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.SurfaceIndex
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.processor.topology.ChunkSize
import dev.adamko.kafkatorio.processor.topology.FactorioServerId
import dev.adamko.kafkatorio.processor.topology.ServerMapChunkId
import dev.adamko.kafkatorio.processor.topology.ServerMapChunkTiles
import java.util.UUID
import kotlinx.serialization.encodeToByteArray
import org.junit.jupiter.api.Test


class Blah {

  @Test
  fun asdad() {

    val tiles = ServerMapChunkTiles<ColourHex>(
      chunkId = ServerMapChunkId(
        serverId = FactorioServerId(UUID.randomUUID().toString()),
        chunkPosition = MapChunkPosition(Int.MAX_VALUE, Int.MAX_VALUE),
        surfaceIndex = SurfaceIndex(UInt.MAX_VALUE),
        chunkSize = ChunkSize.MAX
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
