package dev.adamko.kafkatorio
//
//import java.io.File
//import kotlinx.cli.ArgParser
//import kotlinx.cli.ArgType
//import kotlinx.cli.required
//import kotlinx.coroutines.runBlocking
//
//fun main(args: Array<String>) = runBlocking {
//  val parser = ArgParser("example")
//
//  val distributionZip: File by parser.option(FileArg).required()
//  val modName: String by parser.option(ArgType.String).required()
//  val portalApiKey: String by parser.option(ArgType.String).required()
//  val portalUploadEndpoint: String by parser.option(ArgType.String).required()
//
//  parser.parse(args)
//
//  val client = FactorioModPortalPublishClient(
//    distributionZip,
//    modName,
//    portalApiKey,
//    portalUploadEndpoint,
//  )
//
//  client.uploadMod()
//}
//
//
//object FileArg : ArgType<File>(true) {
//  override val description: kotlin.String
//    get() = "{ File }"
//
//  override fun convert(value: kotlin.String, name: kotlin.String): File {
//    return File(value).also {
//      require(it.exists()) { "${it.canonicalPath} does not exist" }
//      require(it.isFile) { "${it.canonicalPath} is not a file" }
//    }
//  }
//}
