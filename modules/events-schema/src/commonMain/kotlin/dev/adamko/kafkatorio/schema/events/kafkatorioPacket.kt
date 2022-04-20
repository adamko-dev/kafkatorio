package dev.adamko.kafkatorio.schema.events
//
//import dev.adamko.kafkatorio.schema.SerializerProvider
//import dev.adamko.kafkatorio.schema.SerializerProviderDeserializer
//import dev.adamko.kafkatorio.schema.common.Tick
//import kotlin.reflect.KClass
//import kotlin.reflect.KType
//import kotlin.reflect.typeOf
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.Serializable
//
//
////@Serializable
////data class KafkatorioPacket(
////  val modVersion: String,
////  val tick: Tick,
////  val data: KafkatorioPacketData,
////)
//
//
//@Serializable(with = KafkatorioPacketData.JsonSerializer::class)
//sealed class KafkatorioPacketData {
//  abstract val updateType: KafkatorioPacketDataType
//
//  object JsonSerializer : SerializerProviderDeserializer<KafkatorioPacketData>(
//    KafkatorioPacketData::class,
//    KafkatorioPacketData::updateType.name,
//    { json -> KafkatorioPacketDataType.entries.firstOrNull { it.name == json } }
//  )
//}
//
//
///** Discriminator for [KafkatorioPacketData] */
//@Serializable
//enum class KafkatorioPacketDataType(
//  override val serializer: KSerializer<out KafkatorioPacketData>
//) : SerializerProvider<KafkatorioPacketData> {
//
//  // non-keyed
//  CONFIG(ConfigurationUpdate.serializer()),
//  CONSOLE_CHAT(ConsoleChatUpdate.serializer()),
//  CONSOLE_COMMAND(ConsoleCommandUpdate.serializer()),
//  PROTOTYPES(PrototypesUpdate.serializer()),
//  SURFACE(SurfaceUpdate.serializer()),
//
//  // keyed
//  ENTITY(EntityUpdate.serializer()),
//  MAP_CHUNK(MapChunkUpdate.serializer()),
//  PLAYER(PlayerUpdate.serializer()),
//  ;
//
//  companion object {
//    val entries: Set<KafkatorioPacketDataType> = values().toSet()
//  }
//}
//
////inline fun <reified T: KafkatorioPacketData> KafkatorioPacketDataType.isType (): KClass<out T> {
////  return when (this) {
////    KafkatorioPacketDataType.CONFIG          -> ConfigurationUpdate::class
////    KafkatorioPacketDataType.CONSOLE_CHAT    -> ConsoleChatUpdate::class
////    KafkatorioPacketDataType.CONSOLE_COMMAND -> ConsoleCommandUpdate::class
////    KafkatorioPacketDataType.PROTOTYPES      -> PrototypesUpdate::class
////    KafkatorioPacketDataType.SURFACE         -> SurfaceUpdate::class
////    KafkatorioPacketDataType.ENTITY          -> EntityUpdate::class
////    KafkatorioPacketDataType.MAP_CHUNK       -> MapChunkUpdate::class
////    KafkatorioPacketDataType.PLAYER          -> PlayerUpdate::class
////  }
////}
