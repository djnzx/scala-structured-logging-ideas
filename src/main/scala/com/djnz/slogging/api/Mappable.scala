package com.djnz.slogging.api

import com.djnz.slogging.api.Representable.PayloadElements
import io.circe.Json
import io.circe.syntax.EncoderOps

import java.util.{Map => JMap}
import scala.jdk.CollectionConverters.MapHasAsScala

trait Mappable[A] extends Representable[A] {
  def toMap(a: A)(implicit clazz: Class[_]): Map[String, String]
  override def represent(a: A)(implicit clazz: Class[_]): PayloadElements =
    toMap(a).map {
      case (k, v) => (k, v.asJson)
    }
}

trait MappableNested[A] extends Representable[A] {
  def toMap(a: A)(implicit clazz: Class[_]): Map[String, Json]
  override def represent(a: A)(implicit clazz: Class[_]): PayloadElements =
    toMap(a)
}

trait MappableJava[A] extends Representable[A] {
  def toMap(a: A)(implicit clazz: Class[_]): JMap[String, String]
  override def represent(a: A)(implicit clazz: Class[_]): PayloadElements =
    toMap(a).asScala.map { case (k, v) => (k, v.asJson) }
}
