package com.djnz.slogging.api

sealed trait Severity

object Severity {
  case object Error extends Severity
  case object Warn  extends Severity
  case object Info  extends Severity
  case object Debug extends Severity
  case object Trace extends Severity
}
