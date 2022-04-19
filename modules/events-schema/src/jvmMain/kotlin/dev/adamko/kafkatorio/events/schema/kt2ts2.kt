package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.schema2.KafkatorioPacket2
import dev.adamko.kxstsgen.KxsTsConfig
import dev.adamko.kxstsgen.KxsTsGenerator
import dev.adamko.kxstsgen.core.TsDeclaration.TsTypeAlias
import dev.adamko.kxstsgen.core.TsElementId
import dev.adamko.kxstsgen.core.TsLiteral
import dev.adamko.kxstsgen.core.TsTypeRef
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlinx.serialization.builtins.serializer


@ExperimentalUnsignedTypes
fun main(args: Array<String>) {

  val config = KxsTsConfig(
//    typeAliasTyping = BrandTyping
  )

  val gen2 = KxsTsGenerator(config)

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
//@formatter:on
  )

  val definitions = gen2.generate(
    KafkatorioPacket2.serializer(),
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
