package com.djnz.slogging.api

import cats.Monoid
import cats.implicits.catsSyntaxSemigroup
import com.djnz.slogging.api.Representable.PayloadElements
import io.circe.Json
import io.circe.syntax.EncoderOps

trait Representable[A] {
  def represent(a: A)(implicit clazz: Class[_]): PayloadElements
}

object Representable {

  private type PayloadElement = (String, Json)
  type PayloadElements = Iterable[PayloadElement]

  /** the idea is to hide that PayloadElements is a just Map[String, Jsom] */
  object PayloadElements {
    def empty: PayloadElements = Iterable.empty
  }

  implicit val monoid: Monoid[PayloadElements] = new Monoid[PayloadElements] {
    override def empty: PayloadElements = PayloadElements.empty
    override def combine(x: PayloadElements, y: PayloadElements): PayloadElements = x ++ y
  }

  @inline
  def mkMsgTuple(message: String): (String, String) =
    "message" -> message

  def messageJson(message: String): PayloadElement =
    Representable.mkMsgTuple(message) match {
      case (f, v) => f -> v.asJson
    }

  /** do not use getCanonicalName, it fails on inner classes and lambdas */
  def className(clazz: Class[_]): (String, String) =
    "class" -> clazz.getName

  def classNameJson(clazz: Class[_]): PayloadElement =
    Representable.className(clazz) match {
      case (f, v) => f -> v.asJson
    }

  def throwableToStrings(t: Throwable): Array[String] =
    t.toString +: t.getStackTrace.map(_.toString)

  def mkStackTrace(t: Throwable): String = {
    val DELIM = "---------- nested ---------"

    val stringsMain: Array[String] = throwableToStrings(t)

    val stringsNested: Array[String] =
      Option(t.getCause)
        .map(throwableToStrings)
        .map(DELIM +: _)
        .getOrElse(Array.empty)

    (stringsMain ++ stringsNested).mkString("\n ")
  }

  def stackTrace[A <: Throwable](t: A): (String, String) =
    "stackTrace" -> mkStackTrace(t)

  def severityLine(sev: Severity): PayloadElement =
    "severity" -> sev.toString.asJson

  def enrichWithSeverity(payload: PayloadElements, severity: Severity): PayloadElements =
    payload |+| Iterable(severityLine(severity))

  implicit class Syntax[A](private val a: A) extends AnyVal {

    def toIter(implicit ra: Representable[A], c: Class[_]): PayloadElements =
      ra.represent(a)

    def compact(severity: Severity)(implicit ra: Representable[A], c: Class[_]): String =
      enrichWithSeverity(ra.represent(a), severity)
        .toMap
        .asJson
        .noSpaces
  }

}
