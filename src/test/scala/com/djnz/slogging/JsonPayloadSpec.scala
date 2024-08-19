package com.djnz.slogging

import io.circe.Json
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters.MapHasAsJava

class JsonPayloadSpec extends AnyFunSpec with Matchers {

  /** plain Scala Object */
  val plainScalaObj: Map[String, Any] = Map(
    "a" -> Map(
      "b" -> "B",
      "c" -> Map(
        "d" -> true,
        "e" -> 33,
        "f" -> 3.4
      ),
      "g" -> true
    ),
    "h" -> 777
  )

  // TODO: write function to convert plainScalaObj => javaObj
  /** plain java object for protobuf usage */
  val javaObj = Map(
    "a" -> Map(
      "b" -> "B",
      "c" -> Map(
        "d" -> true,
        "e" -> 33,
        "f" -> 3.4
      ).asJava,
      "g" -> true
    ).asJava,
    "h" -> 777
  ).asJava

  // TODO: write function to convert plainScalaObj => circe Map[String, Json]
  /** scala object of type Map[String, Json] to be used by circe */
  val circeObj = Map[String, Json](
    "a" -> Map(
      "b" -> "B".asJson,
      "c" -> Map(
        "d" -> true.asJson,
        "e" -> 33.asJson,
        "f" -> 3.4.asJson
      ).asJson,
      "g" -> true.asJson
    ).asJson,
    "h" -> 777.asJson
  )

  it("2") {
    val json = circeObj.asJson.spaces2
    println(json)
  }

}
