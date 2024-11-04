package Services.Logging

import Core.Errors.Loggable

trait LoggerService {
  def log(message: String)(implicit file: sourcecode.File, line: sourcecode.Line, method: sourcecode.Name): Unit
  def log(event: Loggable): Unit
}