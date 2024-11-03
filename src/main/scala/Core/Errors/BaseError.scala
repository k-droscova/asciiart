package Core.Errors

import sourcecode.{File, Line, Name}

/**
 * BaseError class representing an error that extends both Throwable and Loggable.
 */
case class BaseError(
                      override val message: String,
                      override val severity: LogSeverity = LogSeverity.Error,
                      override val context: LogContext,
                      errorCode: ErrorCodes = GeneralErrorCodes.UnknownError,
                      cause: Option[Throwable] = None
                    )(implicit val file: File, val name: Name, val line: Line) extends Throwable(message, cause.orNull) with Loggable {

  override def toString: String = {
    super.toString.concat(s", Error code: ${errorCode.code}")
  }
}