package UI.CommandLineParsers

import Core.Errors.BaseError

/**
 * Abstract class for command line parsers that accept a single input from multiple specialized parsers.
 *
 * The `SingleInputCommandLineParser` processes command line arguments by delegating parsing to a list
 * of specialized parsers. It validates the results to ensure that exactly one valid input is selected.
 * If no input or multiple inputs are detected, it throws appropriate errors defined by the subclass.
 *
 * @param parsers A list of specialized command line parsers. Each parser is responsible for detecting and
 *                validating its specific argument(s) and returning a result of type `T`.
 * @tparam T The type of the value parsed from the command line arguments.
 */
abstract class SingleInputCommandLineParser[T](
                                                private val parsers: List[SpecializedCommandLineParser[? <: T]]
                                              ) extends CommandLineParser[T] {
  override def parse(args: Array[String]): T = {
    val results = parsers.map(_.parse(args))

    val successfulParsers = results.collect { case Right(Some(value)) => value }
    val errors = results.collect { case Left(error) => error }

    validateParsers(successfulParsers, errors)
  }

  /**
   * Validates the results from specialized parsers.
   *
   * @param successfulParsers A list of successful results.
   * @param errors            A list of errors returned by the parsers.
   * @return A single valid result or throws an error.
   */
  protected def validateParsers(successfulParsers: List[T], errors: List[BaseError]): T = {
    val totalInputs = successfulParsers.size + errors.size

    totalInputs match {
      case 0 => // No argument passed
        throw noInputError()

      case 1 => // Exactly one argument passed
        successfulParsers.headOption.getOrElse(throw errors.head)

      case _ => // More than one argument passed
        throw multipleInputError()
    }
  }

  /**
   * Defines the error to throw when no input is detected.
   */
  protected def noInputError(): BaseError

  /**
   * Defines the error to throw when multiple inputs are detected.
   */
  protected def multipleInputError(): BaseError
}
