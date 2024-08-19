package com.djnz.slogging.logger

trait AppLoggerInstance extends ImplicitLoggerInstance {
  implicit val logger: Logger = LoggerStdOut()
}
