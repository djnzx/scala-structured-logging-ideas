package com.djnz.slogging.entity

sealed abstract class EntityNF(val name: String)

object EntityNF {
  case object BrandId extends EntityNF("brandId")
  case object OrderId extends EntityNF("orderId")
  case object PlayerId extends EntityNF("playerId")
}
