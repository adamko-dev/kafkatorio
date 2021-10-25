package dev.adamko.factorioevents.processor.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass




data class FactorioServerLogRecord<DataType : FactorioObjectData>(
  @JsonProperty("mod_version")
  val modVersion: String,
  @JsonProperty("tick")
  val tick: BigInteger,
  @JsonProperty("event_type")
  val eventType: String,
  @JsonProperty("data")
  val data: DataType,
)


@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "object_name",
  defaultImpl = Void::class, // equivalent to null
  visible = true,
)
sealed class FactorioObjectData(
  @JsonProperty("object_name")
  val objectName: String
)

@JsonTypeName("LuaSurface")
class LuaSurfaceData(
  @JsonProperty("name")
  val name: String,
  @JsonProperty("index")
  val index: Int,
  @JsonProperty("daytime")
  val daytime: BigDecimal,
) : FactorioObjectData("LuaSurface")


/*

{
  "tick": 1144860,
  "object_name": "LuaSurface",
  "event_type": "on_tick",
  "data": {
    "name": "nauvis",
    "index": 1,
    "daytime": 0.4942800000457983
  }
}

 */