package dev.adamko.kafkatorio.events.schema


import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
//@SerialName("EVENT")
data class FactorioEvent(
  override val modVersion: String,
  /** the initial Factorio event (`defines.events`) trigger */
  val eventType: String,
  /** game time */
  val tick: UInt,
  val data: FactorioObjectData,
) : KafkatorioPacket() {
  //  @Transient
  @EncodeDefault
  override val packetType: PacketType = PacketType.EVENT

//  companion object {
//    val kxsModule = SerializersModule {
//      contextual(FactorioEvent::class) { types -> serializer(types[0]) }
//    }
//  }

}


//object FactorioEventSerializer : JsonContentPolymorphicSerializer<FactorioEvent<*>>(
//  FactorioEvent::class
//) {
//
//  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FactorioEvent<*>> {
//
//    val objectName: ObjectName? = element
//      .jsonObject[FactorioEvent<*>::data.name]
//      ?.jsonObject
//      ?.get(FactorioObjectData.discriminatorKey)
//      ?.jsonPrimitive
//      ?.contentOrNull
//      ?.let(ObjectName::valueOf)
//
//    val sub = when (objectName) {
//      ObjectName.LuaPlayer          -> PlayerData.serializer()
//      ObjectName.LuaEntity          -> EntityData.serializer()
//      ObjectName.LuaSurface         -> SurfaceData.serializer()
//      ObjectName.MapChunk           -> MapChunk.serializer()
//      ObjectName.LuaTile            -> MapTile.serializer()
//      ObjectName.ConsoleChatMessage -> ConsoleChatMessage.serializer()
//      null                          -> throw IllegalStateException("unknown type $objectName")
//    }
//    return FactorioEvent.serializer(sub)
//  }
//}
