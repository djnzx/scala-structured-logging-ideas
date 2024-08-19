package com.djnz.slogging

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class StringReplaceAllSpec extends AnyFunSpec with Matchers {

  val NL = "\n"
  val TAB = "\t"

  it("1") {
    val src = s"a${NL}b${NL}c"
    val exp = s"a${TAB}b${TAB}c"
    val dst = src.replaceAll(NL, TAB)
    dst shouldBe exp
  }

}
