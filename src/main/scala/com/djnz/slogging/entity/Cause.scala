package com.djnz.slogging.entity

import cats.implicits._
import com.djnz.slogging.api.Representable.PayloadElements
import com.djnz.slogging.api.Representable._

sealed trait Cause {
  def message: String
  def fields: PayloadElements
  lazy val content: PayloadElements = fields |+| Iterable(messageJson(message))
}

final case class MissingEntity(entity: EntityNF) extends Cause {
  override lazy val message: String = s"Entity not found: ${entity.name}"
  override def fields: PayloadElements = PayloadElements.empty
}

object MissingEntity {
  @inline def apply(entity: EntityNF): Cause = new MissingEntity(entity)
}

final case class TextCause(message: String, fields: PayloadElements = PayloadElements.empty) extends Cause
