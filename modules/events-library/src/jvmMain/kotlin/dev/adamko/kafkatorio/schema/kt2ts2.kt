package dev.adamko.kafkatorio.schema

import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.UnitNumber
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kxstsgen.KxsTsConfig
import dev.adamko.kxstsgen.KxsTsConfig.TypeAliasTypingConfig.BrandTyping
import dev.adamko.kxstsgen.KxsTsGenerator
import dev.adamko.kxstsgen.core.TsDeclaration.TsTypeAlias
import dev.adamko.kxstsgen.core.TsElementId
import dev.adamko.kxstsgen.core.TsLiteral
import dev.adamko.kxstsgen.core.TsSourceCodeGenerator
import dev.adamko.kxstsgen.core.TsTypeRef
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlinx.serialization.builtins.serializer
import org.intellij.lang.annotations.Language


fun main(args: Array<String>) {

  val config = KxsTsConfig(
    typeAliasTyping = BrandTyping
  )

  val gen2 = KxsTsGenerator(
    config = config,
    sourceCodeGenerator = MySourceCodeGenerator(config),
  )

//  gen2.serializerDescriptorOverrides += mapOf(
////@formatter:off
//    SurfaceIndex.serializer() to emptySet(),
//    PlayerIndex.serializer() to emptySet(),
//    UnitNumber.serializer() to emptySet(),
////@formatter:on
//  )

  gen2.descriptorOverrides += mapOf(
//@formatter:off
    Float .serializer().descriptor to TsTypeAlias(TsElementId("kotlin.Float" ), TsTypeRef.Literal(TsLiteral.Custom("float" ), false)),
    Double.serializer().descriptor to TsTypeAlias(TsElementId("kotlin.Double"), TsTypeRef.Literal(TsLiteral.Custom("double"), false)),
    Int   .serializer().descriptor to TsTypeAlias(TsElementId("kotlin.Int"   ), TsTypeRef.Literal(TsLiteral.Custom("int"   ), false)),
    Byte  .serializer().descriptor to TsTypeAlias(TsElementId("kotlin.Byte"  ), TsTypeRef.Literal(TsLiteral.Custom("int8"  ), false)),
    UInt  .serializer().descriptor to TsTypeAlias(TsElementId("kotlin.UInt"  ), TsTypeRef.Literal(TsLiteral.Custom("uint"  ), false)),
    UByte .serializer().descriptor to TsTypeAlias(TsElementId("kotlin.UByte" ), TsTypeRef.Literal(TsLiteral.Custom("uint8" ), false)),
    UShort.serializer().descriptor to TsTypeAlias(TsElementId("kotlin.UShort"), TsTypeRef.Literal(TsLiteral.Custom("uint16"), false)),
    ULong .serializer().descriptor to TsTypeAlias(TsElementId("kotlin.ULong" ), TsTypeRef.Literal(TsLiteral.Custom("uint64"), false)),

    PlayerIndex.serializer().descriptor to TsTypeAlias(TsElementId("PlayerIndex" ), TsTypeRef.Literal(TsLiteral.Custom("PlayerIndex"), false)),
    SurfaceIndex.serializer().descriptor to TsTypeAlias(TsElementId("SurfaceIndex" ), TsTypeRef.Literal(TsLiteral.Custom("SurfaceIndex"), false)),
    UnitNumber.serializer().descriptor to TsTypeAlias(TsElementId("UnitNumber" ), TsTypeRef.Literal(TsLiteral.Custom("UnitNumber"), false)),
//@formatter:on
  )

  val definitions = gen2.generate(
    KafkatorioPacket.serializer(),
//    KafkatorioKeyedPacketKey2.serializer(),
  )


  when (val filename = args.firstOrNull()) {
    null -> println(definitions)
    else -> {
      val outPath = Path(filename)
      outPath.createDirectories()
      val outFile = outPath.resolve("kafkatorio-schema.ts").toFile()
      outFile.printWriter().use {
        it.println(kt2tsHeader)
        it.print(definitions)
      }
    }
  }
}

@Language("TypeScript")
val kt2tsHeader = """
  |// Generated by kxs-ts-gen - do not edit this file manually
  |
""".trimMargin()


class MySourceCodeGenerator(
  config: KxsTsConfig,
) : TsSourceCodeGenerator.Default(config) {

  private val seenTypeAliases: MutableSet<TsElementId> = mutableSetOf()

  override fun generateTypeAlias(element: TsTypeAlias): String {

    if (!seenTypeAliases.add(element.id)) {
      return ""
    }

    return if (element.id.name in listOf("PlayerIndex", "SurfaceIndex", "UnitNumber")) {
      ""
    } else if (
      (element.typeRef as? TsTypeRef.Literal)?.element is TsLiteral.Custom
      || element.id.name == "Tick"
      || element.id.name == "FactorioEntityUpdateEntityDictionary"
      || element.id.name == "FactorioEntityUpdateResourceAmountDictionary"
    ) {
      val aliasedRef = generateTypeReference(element.typeRef)
      """
        |export type ${element.id.name} = ${aliasedRef};
      """.trimMargin()
    } else {
      super.generateTypeAlias(element)
    }
  }
}
