package dev.adamko.kafkatorio.schema.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("kafkatorio.resource.MinedProduct")
sealed interface MinedProduct {

  val resultProtoId: PrototypeId
  //Amount of the item or fluid to give. If not specified, amount_min, amount_max and probability must all be specified.
  val amount: Double?

//Minimal amount of the item or fluid to give. Has no effect when amount is specified.
//amount_min : Uint or double?,

//Maximum amount of the item or fluid to give. Has no effect when amount is specified.
//amount_max : Uint or double?,

//A value in range [0, 1]. Item or fluid is only given with this probability; otherwise no product is produced.
//probability : Double?,

  //How much of this product is a catalyst.
//catalyst_amount : Uint or double?,


  @Serializable
  @SerialName("kafkatorio.resource.MinedProduct.MinedProductFluid")
  data class Fluid(
    override val amount: Double? = null,
    override val resultProtoId: PrototypeId,
  ) : MinedProduct


  @Serializable
  @SerialName("kafkatorio.resource.MinedProduct.MinedProductItem")
  data class Item(
    override val amount: Double? = null,
    override val resultProtoId: PrototypeId,
  ) : MinedProduct


}
