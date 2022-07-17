package dev.adamko.kafkatorio.schema.common

import dev.adamko.kafkatorio.library.LuaJsonList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("kafkatorio.prototype.FactorioPrototype")
sealed interface FactorioPrototype {

  val protoId: PrototypeId


  @SerialName("kafkatorio.prototype.MapTile")
  @Serializable
  data class MapTile(
    override val protoId: PrototypeId,

    val mapColour: Colour,

    val layer: UInt,
//    @Contextual
    val collisionMasks: LuaJsonList<String>,
    val order: String,
    /** Can the tile be mined for resources? */
    val canBeMined: Boolean = false,
  ) : FactorioPrototype


  /**
   * @param[colour] The colour of the prototype, or `null` if the prototype doesn't have colour.
   * @param[mapColour] The map colour used when charting this entity if a
   * [friendly][mapColourFriend] or [enemy][mapColourEnemy] colour isn't defined or `null`.
   * @param[mapColourFriend] The friendly map colour used when charting this entity.
   * @param[mapColourEnemy] The enemy map colour used when charting this entity.
   */
  @SerialName("kafkatorio.prototype.Entity")
  @Serializable
  data class Entity(
    override val protoId: PrototypeId,

    val group: ItemGroup,
    val subgroup: ItemGroup,

    val colour: Colour? = null,
    val mapColour: Colour? = null,
    val mapColourFriend: Colour? = null,
    val mapColourEnemy: Colour? = null,

    val maxHealth: Float,
    val isBuilding: Boolean = false,
    val isEntityWithOwner: Boolean = false,
    val isMilitaryTarget: Boolean = false,
    val miningProperties: MiningProperties? = null,

    val collisionBox: MapBoundingBox? = null,
  ) : FactorioPrototype {

    val tileHeight: Int? get() = collisionBox?.tileHeight
    val tileWidth: Int? get() = collisionBox?.tileWidth

    @Serializable
    @SerialName("kafkatorio.prototype.EntityMiningProperties")
    data class MiningProperties(
      val canBeMined: Boolean = false,
//      @Contextual
      val products: LuaJsonList<MinedProduct>?,
    )

    @Serializable
    @SerialName("kafkatorio.prototype.EntityItemGroup")
    data class ItemGroup(
      val name: String,
      val type: String,
      val parentName: String? = null,
    )

  }
}
