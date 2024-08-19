package com.djnz.slogging

import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json, JsonObject}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class JsonPayloadMerge extends AnyFunSpec with Matchers {

  it("1") {
    case class Clazz(`class`: String)
    object Clazz {
      implicit val encoder: Encoder[Clazz] = deriveEncoder
    }

    case class Message(message: String)
    object Message {
      implicit val encoder: Encoder[Message] = deriveEncoder
    }

    val c = Clazz("com.djnz.Whatever")
    val m = Message("whatever done in 10 sec")
    val cj: Json = c.asJson
    val mj: Json = m.asJson
    println(cj.spaces2)
    println(mj.spaces2)
    val joined = cj
      .deepMerge(mj)
      .deepMerge(
        Map(
          "a" -> "1",
          "b" -> "2"
        ).asJson
      )
    println(joined)
    println(1.asJson)
  }

  it("2") {
    val j0 = Map[String, Json](
      "x" -> 1.asJson,
      "y" -> Map(
        "y1" -> true.asJson,
        "y2" -> 5.asJson
      ).asJson
    ).asJsonObject

    val j1: JsonObject = Map("a" -> "1").asJsonObject
    val j2: JsonObject = Map("b" -> "2").asJsonObject
    val jm: JsonObject = j0 deepMerge j1 deepMerge j2
    val map: Map[String, Json] = jm.toMap
    println(map)
  }

  it("3") {
    val cMap = Map(
      "a" -> 1.asJson,
      "b" -> "jim".asJson
    ).asJsonObject.toMap

    println(cMap)
  }

}
