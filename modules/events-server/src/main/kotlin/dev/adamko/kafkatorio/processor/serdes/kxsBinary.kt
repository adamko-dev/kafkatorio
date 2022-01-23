package dev.adamko.kafkatorio.processor.serdes

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInput
import java.io.DataInputStream
import java.io.DataOutput
import java.io.DataOutputStream
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule


val kxsBinary: KxsBinary = KxsBinary()

/** [`https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/formats.md#efficient-binary-format`](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/formats.md#efficient-binary-format) */
class KxsBinary(
  override val serializersModule: SerializersModule = EmptySerializersModule
) : BinaryFormat {

  override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
    val output = ByteArrayOutputStream()
    val encoder = KxsDataOutputEncoder(DataOutputStream(output), serializersModule)
    encoder.encodeSerializableValue(serializer, value)
    return output.toByteArray()
  }

  override fun <T> decodeFromByteArray(
    deserializer: DeserializationStrategy<T>,
    bytes: ByteArray
  ): T {
    val input = ByteArrayInputStream(bytes)
    val decoder = KxsDataInputDecoder(DataInputStream(input), serializersModule)
    return decoder.decodeSerializableValue(deserializer)
  }
}

private val byteArraySerializer = ByteArraySerializer()

class KxsDataOutputEncoder(
  private val output: DataOutput,
  override val serializersModule: SerializersModule = EmptySerializersModule,
) : AbstractEncoder() {

  override fun encodeBoolean(value: Boolean) = output.writeByte(if (value) 1 else 0)
  override fun encodeByte(value: Byte) = output.writeByte(value.toInt())
  override fun encodeShort(value: Short) = output.writeShort(value.toInt())
  override fun encodeInt(value: Int) = output.writeInt(value)
  override fun encodeLong(value: Long) = output.writeLong(value)
  override fun encodeFloat(value: Float) = output.writeFloat(value)
  override fun encodeDouble(value: Double) = output.writeDouble(value)
  override fun encodeChar(value: Char) = output.writeChar(value.code)
  override fun encodeString(value: String) = output.writeUTF(value)
  override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = output.writeInt(index)

  override fun beginCollection(
    descriptor: SerialDescriptor,
    collectionSize: Int
  ): CompositeEncoder {
    encodeInt(collectionSize)
    return this
  }

  override fun encodeNull() = encodeBoolean(false)
  override fun encodeNotNullMark() = encodeBoolean(true)

  override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
    if (serializer.descriptor == byteArraySerializer.descriptor)
      encodeByteArray(value as ByteArray)
    else
      super.encodeSerializableValue(serializer, value)
  }

  private fun encodeByteArray(bytes: ByteArray) {
    encodeCompactSize(bytes.size)
    output.write(bytes)
  }

  private fun encodeCompactSize(value: Int) {
    if (value < 0xff) {
      output.writeByte(value)
    } else {
      output.writeByte(0xff)
      output.writeInt(value)
    }
  }
}


class KxsDataInputDecoder(
  private val input: DataInput,
  override val serializersModule: SerializersModule = EmptySerializersModule,
  var elementsCount: Int = 0,
) : AbstractDecoder() {

  private var elementIndex = 0

  override fun decodeBoolean(): Boolean = input.readByte().toInt() != 0
  override fun decodeByte(): Byte = input.readByte()
  override fun decodeShort(): Short = input.readShort()
  override fun decodeInt(): Int = input.readInt()
  override fun decodeLong(): Long = input.readLong()
  override fun decodeFloat(): Float = input.readFloat()
  override fun decodeDouble(): Double = input.readDouble()
  override fun decodeChar(): Char = input.readChar()
  override fun decodeString(): String = input.readUTF()
  override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = input.readInt()

  override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
    if (elementIndex == elementsCount) return CompositeDecoder.DECODE_DONE
    return elementIndex++
  }

  override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
    KxsDataInputDecoder(input, serializersModule, descriptor.elementsCount)

  override fun decodeSequentially(): Boolean = true

  override fun decodeCollectionSize(descriptor: SerialDescriptor): Int =
    decodeInt().also { elementsCount = it }

  override fun decodeNotNullMark(): Boolean = decodeBoolean()

  override fun <T> decodeSerializableValue(
    deserializer: DeserializationStrategy<T>,
    previousValue: T?
  ): T =
    @Suppress("UNCHECKED_CAST")
    when (deserializer.descriptor) {
      byteArraySerializer.descriptor -> decodeByteArray() as T
      else                           -> super.decodeSerializableValue(deserializer, previousValue)
    }

  private fun decodeByteArray(): ByteArray {
    val bytes = ByteArray(decodeCompactSize())
    input.readFully(bytes)
    return bytes
  }

  private fun decodeCompactSize(): Int {
    val byte = input.readByte().toInt() and 0xff
    if (byte < 0xff) return byte
    return input.readInt()
  }

}
