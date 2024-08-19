package com.djnz.slogging.logger

trait ImplicitLoggerInstance extends LoggerInstance {
  implicit val logger: Logger
}
