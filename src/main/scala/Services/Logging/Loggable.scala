package Services.Logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import sourcecode.{File, Line, Name}

/**
 * Trait representing anything that can be logged.
 */
trait Loggable {
  val id: String = UUID.randomUUID().toString
  def message: String
  private def dateString: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
  def severity: LogSeverity
  def context: LogContext
  implicit val file: File
  implicit val line: Line
  implicit val name: Name

  lazy val source: LogSource = LogSource(file.value, name.value, line.value)
  override def toString: String = s"[$severity - $context] ($dateString) - $message"
}

/**
 * Class representing the source of a log event (file, function, line).
 */
case class LogSource(file: String, function: String, line: Int)

/**
 * Severity levels for logging.
 */
sealed trait LogSeverity
object LogSeverity {
  case object Debug extends LogSeverity
  case object Info extends LogSeverity
  case object Warning extends LogSeverity
  case object Error extends LogSeverity
  case object Fatal extends LogSeverity
}

/**
 * Contexts for log events (e.g., which part of the system is involved).
 */
sealed trait LogContext
object LogContext {
  case object System extends LogContext
  case object UI extends LogContext
}