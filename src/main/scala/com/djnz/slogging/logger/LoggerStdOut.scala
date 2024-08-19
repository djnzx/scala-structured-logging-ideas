package com.djnz.slogging.logger

import com.djnz.slogging.api.Representable
import com.djnz.slogging.api.Representable.Syntax
import com.djnz.slogging.api.Severity
import java.io.PrintStream

final class LoggerStdOut(stdOut: PrintStream) extends Logger {

  def log[A](a: A, severity: Severity)(implicit ra: Representable[A], c: Class[_]): Unit = {
    val line: String = a.compact(severity)
    stdOut.println(line)
  }

  def trace[A: Representable](a: => A)(implicit c: Class[_]): Unit = log(a, Severity.Trace)
  def debug[A: Representable](a: => A)(implicit c: Class[_]): Unit = log(a, Severity.Debug)
  def info[A: Representable](a: => A)(implicit c: Class[_]): Unit = log(a, Severity.Info)
  def warn[A: Representable](a: => A)(implicit c: Class[_]): Unit = log(a, Severity.Warn)
  def error[A: Representable](a: => A)(implicit c: Class[_]): Unit = log(a, Severity.Error)

  override def close(): Unit = ()

}

object LoggerStdOut {
  def apply(stdOut: PrintStream): Logger = new LoggerStdOut(stdOut)
  def apply(): Logger = LoggerStdOut(Console.out)
}
