package Services.CommandLineParsing

import scala.util.{Try, Success, Failure}
import Services.Logging.Errors.{BaseError, GeneralErrorCodes}
import Services.Logging.{LogContext, LogSeverity, LogSource}
trait CommandLineParser[T] {
  /**
   * Parses the relevant arguments and returns an appropriate object or action.
   *
   * @param args the map of parsed arguments.
   * @return an object related to the specific command (e.g., ImageLoader, Filter, Output, etc.).
   */
  def parse(args: Map[String, String]): T

  /**
   * Validates the command-line arguments to ensure correctness.
   *
   * @param args the map of parsed arguments.
   * @param conflictingArgs a list of tuples containing mutually exclusive arguments.
   * @throws BaseError if validation fails.
   */
  def validateConflictingArgs(args: Map[String, String], conflictingArgs: List[(String, String)]): Unit = {
    conflictingArgs.foreach { case (arg1, arg2) =>
      if (args.contains(arg1) && args.contains(arg2)) {
        throw BaseError(
          message = s"Conflicting arguments: $arg1 and $arg2 cannot be used together.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        )
      }
    }
  }

  /**
   * Utility function to validate that a string can be converted to an integer.
   *
   * @param value the string to validate.
   * @param argName the name of the argument for error reporting.
   * @return the integer value if validation passes.
   * @throws BaseError if the string cannot be converted to an integer.
   */
  def validateInt(value: String, argName: String): Int = {
    Try(value.toInt) match {
      case Success(v) => v
      case Failure(_) =>
        throw BaseError(
          message = s"Argument: $argName with value: $value cannot be converted to integer.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        )
    }
  }
}