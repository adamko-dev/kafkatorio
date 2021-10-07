package example

import org.luaj.vm2.LuaBoolean
import org.luaj.vm2.LuaValue


object Object {
  val field = "A"
}

class Clazz {
  fun memberFunction(p: Int): ULong = 42UL
}

fun forIntegers(b: Byte, s: Short, i: UInt, l: Long) {}
fun forFloats(f: Float, d: Double) {}

fun strings(str: String): String {
  return "That is '$str' from C"
}

val globalString = "A global String"

fun testingLuaBooleanElephant() {
  val b: LuaBoolean = LuaValue.TRUE
  println(b)
}
