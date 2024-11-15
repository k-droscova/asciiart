package Core.Errors

/**
 * BaseError class representing a structured error with message, context, and error code.
 */
case class BaseError(
                      message: String,
                      context: LogContext,
                      errorCode: ErrorCodes = GeneralErrorCodes.UnknownError
                    ) extends Throwable(message) {
  override def toString: String =
    s"Error: $message | Context: ${context.getClass.getSimpleName.replace("$", "")} | Code: ${errorCode.code} (${errorCode.getClass.getSimpleName.replace("$", "")})"
}