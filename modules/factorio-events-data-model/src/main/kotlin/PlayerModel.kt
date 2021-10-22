interface Identifiable<T> {
  val id: T
}


interface IndexedModel {
  val index: UInt
}

interface NamedModel {
  val name: String
}


data class SurfaceModel(
    override val name: String,
    override val index: UInt,
    val daytime: Float,
    val darkness: Float,
    val wind_speed: Double,
) : IndexedModel, NamedModel


/** Player/Entity shared props */
interface ControlModel {
  val driving: Boolean
  val forces: List<ForceModel>
}


//data class PlayerModel(
//    val index: UInt,
//    val characters: List<EntityModel>
//) : Identifiable<UInt> {
//  override val id: UInt = index
//}

//data class EntityModel(
//    val unit_number: Int,
//    override val name: String,
//    val type: String,
//    val active: Boolean,
//    val minable: Boolean,
//    val health: Boolean,
//    val speed: Float, override val driving: Boolean,
//) : ControlModel, NamedModel


data class ForceModel(
    val index: UInt,
    override val name: String,
    val research_progress: Double,
) : Identifiable<UInt>, NamedModel {
  override val id: UInt = index
}
