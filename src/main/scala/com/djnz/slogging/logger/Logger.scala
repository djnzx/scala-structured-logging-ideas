package com.djnz.slogging.logger

import com.djnz.slogging.api.Representable

trait Logger extends AutoCloseable {
  def trace[A: Representable](msg: => A)(implicit c: Class[_]): Unit
  def debug[A: Representable](msg: => A)(implicit c: Class[_]): Unit
  def info[A: Representable](msg: => A)(implicit c: Class[_]): Unit
  def warn[A: Representable](msg: => A)(implicit c: Class[_]): Unit
  def error[A: Representable](msg: => A)(implicit c: Class[_]): Unit
}
