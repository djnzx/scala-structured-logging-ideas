package com.djnz.slogging

import com.djnz.slogging.api.ClassNameProvider
import com.djnz.slogging.api.Representable
import com.djnz.slogging.api.Representable._
import com.djnz.slogging.api.Severity
import com.djnz.slogging.impl.MappableInstances._
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class EnrichPayloadSpec extends AnyFunSpec with Matchers with ClassNameProvider {

  def rep[A](a: A)(implicit ra: Representable[A]) = ra.represent(a).toMap

  it("0") {
    val data: Map[String, Json] = rep("Hello")

    enrichWithSeverity(data, Severity.Info).toMap.asJson shouldBe Json.obj(
      "message"  -> "Hello".asJson,
      "class"    -> "com.djnz.slogging.EnrichPayloadSpec".asJson,
      "severity" -> "Info".asJson
    )
  }
}
