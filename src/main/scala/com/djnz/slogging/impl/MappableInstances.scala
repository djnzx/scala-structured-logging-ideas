package com.djnz.slogging.impl

import cats.implicits._
import com.djnz.slogging.api.Mappable
import com.djnz.slogging.api.MappableJava
import com.djnz.slogging.api.MappableNested
import com.djnz.slogging.api.Representable
import com.djnz.slogging.api.Representable._
import io.circe.Json
import io.circe.syntax.EncoderOps
import java.util
import java.util.{Map => JMap}

trait MappableInstances {

  /** instance for String logging
    *
    * it generates pair: "message" -> string value
    */
  implicit def rString[S <: CharSequence]: Mappable[S] = new Mappable[S] {
    override def toMap(a: S)(implicit clazz: Class[_]): Map[String, String] =
      Map(
        mkMsgTuple(a.toString),
        className(clazz)
      )
  }

  /** instance for Scala Map logging
    *
    * it generates pairs from the Map:
    *  "k1" -> v1
    *  "k2" -> v2
    */
  implicit def rScalaMap[M <: Map[String, String]]: Mappable[M] = new Mappable[M] {
    override def toMap(a: M)(implicit clazz: Class[_]): Map[String, String] =
      a + className(clazz)
  }

  implicit def rScalaMapNested[M <: Map[String, Json]]: MappableNested[M] = new MappableNested[M] {
    override def toMap(a: M)(implicit clazz: Class[_]): Map[String, Json] =
      a + {
        val (c, n) = className(clazz)
        c -> n.asJson
      }
  }

  /** instance for Java Map logging
    *
    * it generates pairs from the Map:
    *  "k1" -> v1
    *  "k2" -> v2
    *
    *  Variance added to support any subtypes of Java Map:
    *  HashMap, TreeMap, etc
    */
  implicit def rJavaMap[M <: JMap[String, String]]: MappableJava[M] = new MappableJava[M] {
    override def toMap(a: M)(implicit clazz: Class[_]): JMap[String, String] = {
      val m = new util.HashMap[String, String](a)
      className(clazz) match { case (k, v) => m.put(k, v) }
      m
    }
  }

  /** instance for Java Throwable logging
    *
    * it generates entry: "stackTrace" -> List(message + stackTrace)
    *
    * type variance added to work with any subtype of Throwable, not only with Throwable
    */
  implicit def rThrowable[T <: Throwable]: Mappable[T] = new Mappable[T] {
    override def toMap(a: T)(implicit clazz: Class[_]): Map[String, String] =
      Map(
        stackTrace(a),
        className(clazz)
      )
  }

  /** any Tuple2 derivation */
  implicit def rTuple2[A: Representable, B: Representable]: Representable[(A, B)] =
    new Representable[(A, B)] {
      override def represent(t: (A, B))(implicit clazz: Class[_]): PayloadElements = t match {
        case (a, b) => a.toIter |+| b.toIter
      }
    }

  /** any Tuple3 derivation */
  implicit def rTuple3[A: Representable, B: Representable, C: Representable]: Representable[(A, B, C)] =
    new Representable[(A, B, C)] {
      override def represent(t: (A, B, C))(implicit clazz: Class[_]): PayloadElements = t match {
        case (a, b, c) => a.toIter |+| b.toIter |+| c.toIter
      }
    }

  /** any Tuple4 derivation */
  implicit def rTuple4[
      A: Representable,
      B: Representable,
      C: Representable,
      D: Representable
  ]: Representable[(A, B, C, D)] =
    new Representable[(A, B, C, D)] {
      override def represent(t: (A, B, C, D))(implicit clazz: Class[_]): PayloadElements = t match {
        case (a, b, c, d) => a.toIter |+| b.toIter |+| c.toIter |+| d.toIter
      }
    }

  /** any Tuple5 derivation */
  implicit def rTuple5[
      A: Representable,
      B: Representable,
      C: Representable,
      D: Representable,
      E: Representable
  ]: Representable[(A, B, C, D, E)] =
    new Representable[(A, B, C, D, E)] {
      override def represent(t: (A, B, C, D, E))(implicit clazz: Class[_]): PayloadElements = t match {
        case (a, b, c, d, e) => a.toIter |+| b.toIter |+| c.toIter |+| d.toIter |+| e.toIter
      }
    }

}

object MappableInstances extends MappableInstances
