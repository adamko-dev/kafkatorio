package dev.adamko.kafkatorio.schema.prototypes
//
//import dev.adamko.kafkatorio.schema.SerializerProvider
//import dev.adamko.kafkatorio.schema.SerializerProviderDeserializer
//import dev.adamko.kafkatorio.schema.common.Colour
//import dev.adamko.kafkatorio.schema.common.PrototypeName
//import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype.PrototypeObjectName
//import kotlinx.serialization.Contextual
//import kotlinx.serialization.DeserializationStrategy
//import kotlinx.serialization.EncodeDefault
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.JsonContentPolymorphicSerializer
//import kotlinx.serialization.json.JsonElement
//import kotlinx.serialization.json.contentOrNull
//import kotlinx.serialization.json.jsonObject
//import kotlinx.serialization.json.jsonPrimitive
//
//
//@Serializable(with = FactorioPrototype.JsonSerializer::class)
//sealed class FactorioPrototype {
//  @EncodeDefault
//  abstract val prototypeObjectName: PrototypeObjectName
//
//  enum class PrototypeObjectName(
//    override val serializer: KSerializer<out FactorioPrototype>
//  ) : SerializerProvider<FactorioPrototype> {
//    LuaTilePrototype(MapTilePrototype.serializer()),
//    ;
//
//    companion object {
//      val entries: Set<PrototypeObjectName> = values().toSet()
//    }
//  }
//
//  object JsonSerializer : SerializerProviderDeserializer<FactorioPrototype>(
//    FactorioPrototype::class,
//    FactorioPrototype::prototypeObjectName.name,
//    { json -> PrototypeObjectName.entries.firstOrNull { it.name == json } }
//  )
//}
//
//
//
//@Serializable
//data class MapTilePrototype(
//  val name: PrototypeName,
//  val layer: UInt,
//  val mapColour: Colour,
//  @Contextual
//  val collisionMasks: List<String>,
//  val order: String,
//  /** Can the tile be mined for resources? */
//  val canBeMined: Boolean,
//) : FactorioPrototype() {
//  @EncodeDefault
//  override val prototypeObjectName: PrototypeObjectName = PrototypeObjectName.LuaTilePrototype
//}
