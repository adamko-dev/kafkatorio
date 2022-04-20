package dev.adamko.kafkatorio.events.schema

//import dev.adamko.kafkatorio.schema.events.KafkatorioInstantPacketData
//import dev.adamko.kafkatorio.schema.events.KafkatorioKeyedPacketData
//import dev.adamko.kafkatorio.schema.events.KafkatorioKeyedPacketKey
////import dev.adamko.kafkatorio.schema.events.KafkatorioPacket
//import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype
//import kotlin.io.path.Path
//import kotlin.io.path.createDirectories
//import me.ntrrgc.tsGenerator.TypeScriptGenerator
//import org.intellij.lang.annotations.Language
//
///**
// * Convert Kotlin classes to Typescript classes, using [TypeScriptGenerator]
// */
//fun main(args: Array<String>) {
//
//  val gen = TypeScriptGenerator(
//    rootClasses = buildSet {
////      add(KafkatorioPacket::class)
////      addAll(KafkatorioPacket::class.sealedSubclasses)
//
//      add(KafkatorioInstantPacketData::class)
//      addAll(KafkatorioInstantPacketData::class.sealedSubclasses)
//
//      add(KafkatorioKeyedPacketData::class)
//      addAll(KafkatorioKeyedPacketData::class.sealedSubclasses)
//      addAll(KafkatorioKeyedPacketKey::class.sealedSubclasses)
//
//      add(FactorioPrototype::class)
//      addAll(FactorioPrototype::class.sealedSubclasses)
//    },
//    mappings = mapOf(
//      // builtin Factorio numeric types > `typed-factorio/generated/builtin-types.d.ts`
//      Float::class to "float",
//      Double::class to "double",
//      Int::class to "int",
//      Byte::class to "int8",
//      UInt::class to "uint",
//      UByte::class to "uint8",
//      UShort::class to "uint16",
//      ULong::class to "uint64",
//    ),
//    classTransformers = listOf(
//      ValueClassTransformer,
////        PropertyNameTransformer,
//    ),
////      voidType = VoidType.UNDEFINED,
//  )
//
//  val definitions = gen.definitionsText
//
//  when (val filename = args.firstOrNull()) {
//    null -> println(definitions)
//    else -> {
//      val outPath = Path(filename)
//      outPath.createDirectories()
//
//      splitDefinitions(definitions)
//        .forEach { (defFile, def) ->
//          val outFile = outPath.resolve(defFile).toFile()
//          outFile.printWriter().use {
//            it.println(kt2tsHeader)
//            it.print(def)
//          }
//          println("Generated file: $outFile")
//        }
//
//    }
//  }
//}
//
//
///** Split the TypeScript interfaces into different files */
//private fun splitDefinitions(definitions: String): Map<String, String> {
//  return definitions
//    .split("\n\n")
//    .groupBy { def ->
//
//      when {
//        def isInterface "KafkatorioPacket"
//            || def extendsAny listOf("KafkatorioPacket")
//            || def isType "KafkatorioPacketDataType"
//             -> "kafkatorio-packet.d.ts"
//
//        def isInterface "KafkatorioInstantPacketData"
//            || def extends "KafkatorioInstantPacketData"
//            || def isType "KafkatorioInstantPacketDataType"
//
//            || def isInterface "ConfigurationUpdateGameData"
//            || def isInterface "ConfigurationUpdateModData"
//             -> "kafkatorio-instant-packet.d.ts"
//
//        def isInterface "KafkatorioKeyedPacket"
//            || def isInterface "KafkatorioKeyedPacketData"
//            || def extends "KafkatorioKeyedPacket"
//            || def isType "KafkatorioKeyedPacketDataType"
//
//            || def isInterface "KafkatorioKeyedPacketKey"
//             -> "kafkatorio-keyed-packet.d.ts"
//
//        def isInterface "FactorioPrototypes"
//            || def isInterface "FactorioPrototype"
//            || def extends "FactorioPrototype"
//            || def isType "PrototypeObjectName"
//             -> "prototype.d.ts"
//
//        else -> "schema.d.ts"
//      }
//    }
//    .mapValues { (_, def) ->
//      def
//        .sorted()
//        .joinToString("\n\n", postfix = "\n")
//        .replace("    ", "  ")
//    }
//}
//
//@Language("TypeScript")
//val kt2tsHeader = """
//  |// Generated by TypeScriptGenerator - do not edit this file manually
//  |
//""".trimMargin()
//
//private infix fun String.extends(types: String): Boolean =
//  contains(Regex(".+extends.*(${types})"))
//
//private infix fun String.extendsAny(types: List<String>): Boolean =
//  extends(types.joinToString("|") { "$it,".replaceAfterLast(',', "") })
//
//private infix fun String.isInterface(type: String): Boolean =
//  contains(Regex("interface $type.*"))
//
//
//private infix fun String.isType(type: String): Boolean =
//  contains(Regex("type $type.*"))
//
//
////object PropertyNameTransformer : ClassTransformer {
////  override fun transformPropertyName(
////    propertyName: String,
////    property: KProperty<*>,
////    klass: KClass<*>
////  ): String {
////    return camelCaseToSnakeCase(propertyName)
////  }
////}
