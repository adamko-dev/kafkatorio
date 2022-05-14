package dev.adamko.kafkatorio.processor.misc

import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.processor.topology.FactorioServerId
import dev.adamko.kafkatorio.processor.topology.ServerMapChunkId
import dev.adamko.kafkatorio.processor.topology.ServerMapChunkTiles
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.booleanArray
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.charArray
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.doubleArray
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.floatArray
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.intArray
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.longArray
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.shortArray
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uByte
import io.kotest.property.arbitrary.uInt
import io.kotest.property.arbitrary.uLong
import io.kotest.property.arbitrary.uShort
import io.kotest.property.checkAll
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString


class KxsBinaryTests : FunSpec({

  test("byte array") {
    val ba = byteArrayOf(
      4, 116, 101, 115, 116, 2, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 3
    )

    val result = kxsBinary.decodeFromByteArray(ServerMapChunkId.serializer(), ba)

    withClue(ba.toString(Charsets.UTF_8)) {

      result shouldBeEqualToComparingFields ServerMapChunkId(
        serverId = FactorioServerId("test"),
        chunkPosition = MapChunkPosition(1, 2),
        surfaceIndex = SurfaceIndex(1u),
        chunkSize = ChunkSize.CHUNK_064,
      )

    }
  }

  test("encoding") {
    val ba = kxsBinary.encodeToByteArray(
      ServerMapChunkId(
        serverId = FactorioServerId("test"),
        chunkPosition = MapChunkPosition(1, 2),
        surfaceIndex = SurfaceIndex(1u),
        chunkSize = ChunkSize.CHUNK_064,
      )
    )

    withClue(ba.toString(Charsets.UTF_8)) {
      ba shouldBe byteArrayOf(
        4, 116, 101, 115, 116, 2, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 3
      )
    }
  }

  context("inline value class") {
    val uuid = "fa858d73-6205-44cf-aeeb-6bbf3148c4a7"
    //@formatter:off
    val expectedBytes = byteArrayOf(
       36, 102,  97,  56, 53, 56, 100, 55, 51,  45, 54, 50, 48, 53, 45, 52, 52, 99,
      102, 45,  97, 101, 101, 98, 45,  54, 98, 98, 102, 51, 49, 52, 56, 99, 52, 97, 55,
    )
    //@formatter:on

    test("encode") {
      val ba = kxsBinary.encodeToByteArray(FactorioServerId(uuid))

      withClue(ba.toString(Charsets.UTF_8)) {
        ba shouldBe expectedBytes
      }
    }
    test("decode") {
      val ba = kxsBinary.encodeToByteArray(FactorioServerId(uuid))
      val id: FactorioServerId = kxsBinary.decodeFromByteArray(ba)

      id shouldBeEqualToComparingFields FactorioServerId(uuid)
    }
  }

  context("ServerMapChunkTiles") {
    val initial = ServerMapChunkTiles(
      ServerMapChunkId(
        FactorioServerId("server-id"),
        MapChunkPosition(33, 44),
        SurfaceIndex(4u),
        ChunkSize.CHUNK_032,
      ),
      mapOf(
        MapTilePosition(1, 2) to ColourHex(1u, 2u, 3u, 4u),
      )
    )

    test("!json round trip") {
      val jsonEncoded = jsonMapperKafkatorio.encodeToString(initial)
      val jsonDecoded =
        jsonMapperKafkatorio.decodeFromString<ServerMapChunkTiles<ColourHex>>(jsonEncoded)

      jsonDecoded shouldBeEqualToComparingFields initial
    }

    test("binary round trip") {
      val binaryEncoded = kxsBinary.encodeToByteArray(initial)
      val binaryDecoded = kxsBinary.decodeFromByteArray<ServerMapChunkTiles<ColourHex>>(binaryEncoded)

      binaryDecoded shouldBeEqualToComparingFields initial
    }
  }

  context("primitives round trip") {
    test("double") {
      checkAll(Arb.double()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: Double = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("float") {
      checkAll(Arb.float()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: Float = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("int") {
      checkAll(Arb.int()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: Int = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeExactly input
      }
    }

    test("long") {
      checkAll(Arb.long()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: Long = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeExactly input
      }
    }

    test("short") {
      checkAll(Arb.short()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: Short = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("byte") {
      checkAll(Arb.byte()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: Byte = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("boolean") {
      checkAll(Arb.boolean()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: Boolean = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("char") {
      checkAll(Arb.char()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: Char = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("string") {
      checkAll(Arb.string()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: String = kxsBinary.decodeFromByteArray(encoded)
        decoded shouldBeEqualComparingTo input
      }
    }
  }


  @OptIn(ExperimentalUnsignedTypes::class)
  context("unsigned numbers round trip") {
    test("uint") {
      checkAll(Arb.uInt()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(UInt.serializer(), input)
        val decoded: UInt = kxsBinary.decodeFromByteArray(UInt.serializer(), encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("ulong") {
      checkAll(Arb.uLong()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(ULong.serializer(), input)
        val decoded: ULong = kxsBinary.decodeFromByteArray(ULong.serializer(), encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("ushort") {
      checkAll(Arb.uShort()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(UShort.serializer(), input)
        val decoded: UShort = kxsBinary.decodeFromByteArray(UShort.serializer(), encoded)
        decoded shouldBeEqualComparingTo input
      }
    }

    test("ubyte") {
      checkAll(Arb.uByte()) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(UByte.serializer(), input)
        val decoded: UByte = kxsBinary.decodeFromByteArray(UByte.serializer(), encoded)
        decoded shouldBeEqualComparingTo input
      }
    }
  }

  context("primitive arrays round trip") {
    test("double array") {
      checkAll(Arb.doubleArray(Arb.positiveInt(50), Arb.double())) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: DoubleArray = kxsBinary.decodeFromByteArray(encoded)
        decoded.asList() shouldContainExactly input.asList()
      }
    }

    test("float array") {
      checkAll(Arb.floatArray(Arb.positiveInt(50), Arb.float())) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: FloatArray = kxsBinary.decodeFromByteArray(encoded)
        decoded.asList() shouldContainExactly input.asList()
      }
    }

    test("int array") {
      checkAll(Arb.intArray(Arb.positiveInt(50), Arb.int())) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: IntArray = kxsBinary.decodeFromByteArray(encoded)
        decoded.asList() shouldContainExactly input.asList()
      }
    }

    test("long array") {
      checkAll(Arb.longArray(Arb.positiveInt(50), Arb.long())) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: LongArray = kxsBinary.decodeFromByteArray(encoded)
        decoded.asList() shouldContainExactly input.asList()
      }
    }

    test("short array") {
      checkAll(Arb.shortArray(Arb.positiveInt(50), Arb.short())) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: ShortArray = kxsBinary.decodeFromByteArray(encoded)
        decoded.asList() shouldContainExactly input.asList()
      }
    }

    test("byte array") {
      checkAll(Arb.byteArray(Arb.positiveInt(50), Arb.byte())) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: ByteArray = kxsBinary.decodeFromByteArray(encoded)
        decoded.asList() shouldContainExactly input.asList()
      }
    }

    test("boolean array") {
      checkAll(Arb.booleanArray(Arb.positiveInt(50), Arb.boolean())) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: BooleanArray = kxsBinary.decodeFromByteArray(encoded)
        decoded.asList() shouldContainExactly input.asList()
      }
    }

    test("char array") {
      checkAll(Arb.charArray(Arb.positiveInt(50), Arb.char())) { input ->
        val encoded: ByteArray = kxsBinary.encodeToByteArray(input)
        val decoded: CharArray = kxsBinary.decodeFromByteArray(encoded)
        decoded.asList() shouldContainExactly input.asList()
      }
    }
  }
})
