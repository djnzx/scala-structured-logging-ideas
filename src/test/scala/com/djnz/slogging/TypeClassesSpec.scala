package com.djnz.slogging

import com.djnz.slogging.api.ClassNameProvider
import com.djnz.slogging.api.Mappable
import com.djnz.slogging.api.MappableJava
import com.djnz.slogging.api.Representable
import com.djnz.slogging.impl.MappableInstances._
import io.circe.JsonObject
import io.circe.syntax.EncoderOps
import java.util
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.immutable.HashMap
import scala.jdk.CollectionConverters._

class TypeClassesSpec extends AnyFunSpec with Matchers with ClassNameProvider {

  def rep[A](a: A)(implicit ra: Representable[A]) = ra.represent(a).toMap
  def repAsMap[A](a: A)(implicit ma: Mappable[A]) = ma.toMap(a)
  def repAsMapJ[A](a: A)(implicit ma: MappableJava[A]) = ma.toMap(a)

  val CLASS_NAME: String = clazz.getCanonicalName

  /** in this package we implemented instances for:
    *
    * - string
    * - Scala Map
    * - Java Map
    * - Throwable and all sub types
    * - Tuple2
    * - Tuple3
    * - Tuple4
    * - Tuple5
    */
  describe("instances") {

    it("string instance as message") {
      repAsMap("Hello") shouldBe Map(
        "class"   -> CLASS_NAME,
        "message" -> "Hello"
      )
    }

    it("throwable with variance") {
      val rep: Map[String, String] = repAsMap(new IllegalArgumentException("too bad"))
      rep should have size 2
      rep should contain("class" -> CLASS_NAME)
      rep should contain key "stackTrace"
      rep("stackTrace") should include("too bad")
    }

    it("custom map (Scala) Map object") {
      repAsMap(
        Map(
          "a" -> 33.toString,
          "b" -> 44.toString
        )
      ) shouldBe Map(
        "class" -> CLASS_NAME,
        "a"     -> "33",
        "b"     -> "44"
      )
    }

    it("custom map (Scala) HashMap Variance") {
      repAsMap(
        HashMap(
          "a" -> 33.toString,
          "b" -> 44.toString
        )
      ) shouldBe Map(
        "class" -> CLASS_NAME,
        "a"     -> "33",
        "b"     -> "44"
      )
    }

    it("custom map (Java) with variance (HashMap)") {
      val m: util.HashMap[String, String] = new util.HashMap[String, String] {
        {
          put("a", "33")
          put("b", "44")
        }
      }

      repAsMapJ(m) shouldBe Map(
        "class" -> CLASS_NAME,
        "a"     -> "33",
        "b"     -> "44"
      ).asJava
    }

    it("custom map (Java) with variance (TreeMap)") {
      val m: util.TreeMap[String, String] = new util.TreeMap[String, String] {
        {
          put("a", "33")
          put("b", "44")
        }
      }

      repAsMapJ(m) shouldBe Map(
        "class" -> CLASS_NAME,
        "a"     -> "33",
        "b"     -> "44"
      ).asJava
    }

    it("any tuple2 combination") {
      rep(
        "Jim",
        Map(
          "a" -> 33.toString,
          "b" -> 44.toString
        )
      ) shouldBe Map(
        "class"   -> CLASS_NAME.asJson,
        "message" -> "Jim".asJson,
        "a"       -> "33".asJson,
        "b"       -> "44".asJson
      )
    }

    it("any tuple3 combination") {
      val rep0: JsonObject = rep(
        "Jim",
        new IndexOutOfBoundsException,
        Map(
          "a" -> 33.toString,
          "b" -> 44.toString
        )
      ).asJsonObject

      rep0 should have size 5

      rep0.contains("class") shouldBe true
      rep0.contains("stackTrace") shouldBe true
      rep0.contains("message") shouldBe true
      rep0.contains("a") shouldBe true
      rep0.contains("b") shouldBe true
    }

  }

}
