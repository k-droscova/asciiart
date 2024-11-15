package Core.Errors

/**
 * Contexts for log events (e.g., which part of the system is involved).
 */
sealed trait LogContext
object LogContext {
  case object System extends LogContext
  case object UI extends LogContext
}