package dev.adamko.kafkatorio.events.schema

import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import me.ntrrgc.tsGenerator.TypeScriptGenerator
import org.intellij.lang.annotations.Language

/**
 * Convert Kotlin classes to Typescript classes, using [TypeScriptGenerator]
 */
fun main(args: Array<String>) {

  val gen =
    TypeScriptGenerator(
      rootClasses = buildSet {
        addAll(KafkatorioPacket::class.sealedSubclasses)

        addAll(FactorioObjectData::class.sealedSubclasses)

        addAll(FactorioPrototype::class.sealedSubclasses)
        add(FactorioPrototypes::class)
        add(FactorioConfigurationUpdate::class)


        add(FactorioEventUpdatePacket::class)
        addAll(FactorioEventUpdate::class.sealedSubclasses)
        addAll(FactorioEventUpdateKey::class.sealedSubclasses)
//        addAll(FactorioEventUpdateData::class.sealedSubclasses)

      },
      mappings = mapOf(
        // builtin Factorio numeric types > `typed-factorio/generated/builtin-types.d.ts`
        Float::class to "float",
        Double::class to "double",
        Int::class to "int",
        Byte::class to "int8",
        UInt::class to "uint",
        UByte::class to "uint8",
        UShort::class to "uint16",
        ULong::class to "uint64",
      ),
      classTransformers = listOf(
        ValueClassTransformer,
//        PropertyNameTransformer,
      ),
//      voidType = VoidType.UNDEFINED,
    )

  val definitions = gen.definitionsText

  when (val filename = args.firstOrNull()) {
    null -> println(definitions)
    else -> {
      val outPath = Path(filename)
      outPath.createDirectories()

      splitDefinitions(definitions)
        .forEach { (defFile, def) ->
          val outFile = outPath.resolve(defFile).toFile()
          outFile.printWriter().use {
            it.println(header)
            it.print(def)
          }
          println("Generated file: $outFile")
        }

    }
  }
}

/** Split the TypeScript interfaces into different files */
private fun splitDefinitions(definitions: String): Map<String, String> {
  return definitions
    .split("\n\n")
    .groupBy { def ->


      when {

        "interface FactorioObjectData " in def ||
            "type ObjectName " in def ||
            def.contains(Regex("interface.+extends.+(FactorioObjectData).*"))
                                               -> "object-data.d.ts"

        "extends FactorioPrototype " in def ||
            "interface FactorioPrototypes " in def ||
            "interface FactorioPrototype " in def ||
            "type PrototypeObjectName " in def -> "prototype.d.ts"

        "extends FactorioConfigurationUpdate " in def ||
            "interface FactorioConfigurationUpdate " in def ||
            "FactorioGameDataUpdate " in def ||
            "FactorioModInfo " in def          -> "config-update.d.ts"

        "interface FactorioEventUpdatePacket" in def ||
            "type FactorioEventUpdateType =" in def ||
            "interface FactorioEventUpdateKey {" in def ||
            "interface FactorioEventUpdateData {" in def ||
            def.contains(
              Regex(
                """
                interface.+extends.+(FactorioEventUpdate|PlayerUpdateKey|PlayerUpdateData).*
                """.trimIndent()
              )
            )
                                               -> "update.d.ts"

        else                                   -> "schema.d.ts"
      }
    }
    .mapValues { (_, def) ->
      def
        .joinToString("\n\n", postfix = "\n")
        .replace("    ", "  ")
    }
}

@Language("TypeScript")
val header = """
  |// Generated by TypeScriptGenerator - do not edit this file manually
  |
""".trimMargin()

//object PropertyNameTransformer : ClassTransformer {
//  override fun transformPropertyName(
//    propertyName: String,
//    property: KProperty<*>,
//    klass: KClass<*>
//  ): String {
//    return camelCaseToSnakeCase(propertyName)
//  }
//}
