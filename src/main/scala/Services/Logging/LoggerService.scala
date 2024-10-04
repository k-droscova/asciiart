package Services.Logging

trait LoggerService {
  def log(message: String)(implicit file: sourcecode.File, line: sourcecode.Line, method: sourcecode.Name): Unit
  def log(event: Loggable): Unit
}

class ConsoleLoggerService extends LoggerService {

  def log(message: String)(implicit file: sourcecode.File, line: sourcecode.Line, method: sourcecode.Name): Unit = {
    println(s"[${file.value}:${method.value}:${line.value}]")
    println(s"$message")
  }

  def log(event: Loggable): Unit = {
    println(s"[${event.source.file}:${event.source.function}:${event.source.line}]")
    println(s"${event.toString}")
  }
}