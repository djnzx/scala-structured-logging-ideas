package com.djnz.slogging.entity

import io.circe.Encoder
import io.circe.Json
import io.circe.syntax.EncoderOps
import cats.implicits._
import com.djnz.slogging.api.Representable
import com.djnz.slogging.api.Representable._

import java.util.UUID

trait BaseLoggingEntity {
  val cause: Cause
  val fields: PayloadElements
  def clazz: Class[_]

  lazy val fullContent: PayloadElements =
    cause.content |+| Iterable(classNameJson(clazz)) |+| fields


  @inline def withKV(key: String, value: Json): BaseLoggingEntity

  @inline def withKV(newFields: Map[String, Json]): BaseLoggingEntity

  @inline def withKV[V: Encoder](key: String, value: V): BaseLoggingEntity =
    withKV(key, value.asJson)

  @inline def withKV[V: Encoder](kvs: (String, V)*): BaseLoggingEntity =
    withKV(kvs.toMap)

  @inline def withKV[V: Encoder](kv: (String, V)): BaseLoggingEntity =
    withKV(kv._1, kv._2)

  @inline def withKV[V: Encoder](kvs: Map[String, V]): BaseLoggingEntity =
    withKV(kvs.map { case (k, v) => (k, v.asJson) })

  @inline def withException[T <: Throwable](x: T): BaseLoggingEntity =
    withKV(stackTrace(x))

  @inline def withPlayerId(playerId: UUID): BaseLoggingEntity =
    withKV("playerId", playerId)

  @inline def withBrandId(brandId: String): BaseLoggingEntity =
    withKV("brandId", brandId)
}

object BaseLoggingEntity {
  implicit val representable: Representable[BaseLoggingEntity] = new Representable[BaseLoggingEntity] {
    override def represent(a: BaseLoggingEntity)(implicit clazz: Class[_]): PayloadElements = a.fullContent
  }
  implicit val encoder: Encoder[BaseLoggingEntity] = Encoder.instance(_.fullContent.toMap.asJson)

}

case class LoggingEntity private (
    cause: Cause,
    fields: PayloadElements = PayloadElements.empty
  )(implicit
    val clazz: Class[_])
    extends BaseLoggingEntity {

  @inline override def withKV(key: String, value: Json): BaseLoggingEntity =
    copy(fields = fields |+| Iterable(key -> value.asJson))

  @inline override def withKV(newFields: Map[String, Json]): BaseLoggingEntity =
    copy(fields = fields |+| newFields)
}

object LoggingEntity {

  @inline def apply(cause: Cause)(implicit c: Class[_]): BaseLoggingEntity = new LoggingEntity(cause)

  @inline def apply(message: String)(implicit c: Class[_]): BaseLoggingEntity = apply(TextCause(message))

  @inline def playerIdNotFound(playerId: UUID)(implicit c: Class[_]): BaseLoggingEntity =
    LoggingEntity(MissingEntity(EntityNF.PlayerId))
      .withPlayerId(playerId)

}
