package UI.CommandLineParsers

import Core.Errors.BaseError

/**
 * A trait defining a parser for specialized command line arguments.
 *
 * The parser detects its specific argument(s) in the input, validates them,
 * and returns the appropriate result for the specified type `T`.
 *
 * @tparam T The type of the result produced by this parser.
 */
trait SpecializedCommandLineParser[T] {

  /**
   * Parses the provided command line arguments and determines whether this parser's specific
   * argument(s) are present, valid, and can be used to produce a value of type `T`.
   *
   * The return value can be one of the following:
   * - `Right(None)`: No matching argument was detected in the input.
   * - `Right(Some(T))`: A valid argument was detected, successfully parsed, and a corresponding value of type `T` was produced.
   * - `Left(BaseError)`: A matching argument was detected, but an error occurred during parsing or validation.
   *
   * @param args The command line arguments to parse.
   * @return Either a `BaseError` (Left) or an `Option[T]` (Right):
   *         - `Right(None)` if no relevant argument is found.
   *         - `Right(Some(T))` if the argument is valid and successfully parsed.
   *         - `Left(BaseError)` if the argument is detected but parsing fails.
   */
  def parse(args: Array[String]): Either[BaseError, Option[T]]
}