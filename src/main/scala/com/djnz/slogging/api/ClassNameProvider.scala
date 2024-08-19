package com.djnz.slogging.api

trait ClassNameProvider {
  implicit val clazz: Class[_] = getClass
}
