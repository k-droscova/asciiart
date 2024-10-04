package Services.Logging

/**
 * BaseError class representing an error that extends both Throwable and Loggable.
 */
case class BaseError(
                      override val message: String,
                      override val severity: LogSeverity = LogSeverity.Error,
                      override val context: LogContext,
                      override val source: LogSource,
                      cause: Option[Throwable] = None,
                      val code: Int = 1
                    ) extends Throwable(message, cause.orNull) with Loggable {}