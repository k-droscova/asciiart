package UI.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers.*
import UI.CommandLineParsers.SingleInputCommandLineParser

/**
 * Implementation of `AsciiTableCommandLineParser` that supports multiple ASCII table types.
 *
 * The `AsciiTableCommandLineParserImpl` delegates the parsing logic to a list of specialized parsers,
 * each responsible for detecting and validating a specific table type. It ensures that exactly one table
 * type is selected and provides meaningful errors for invalid or conflicting inputs.
 *
 * Supported Table Types:
 * - `--table=custom`: Specifies a custom ASCII table with user-defined characters.
 * - `--table=default`: Uses the default ASCII table for conversion.
 * - `--table=bourke`: Uses the Bourke ASCII table for conversion.
 * - `--table=bordered`: Specifies a bordered ASCII table with custom borders.
 *
 * @param parsers A list of specialized parsers for the supported table types. Defaults to the following parsers:
 *                - `CustomLinearAsciiTableCommandLineParser`
 *                - `DefaultLinearAsciiTableCommandLineParser`
 *                - `BourkeLinearAsciiTableCommandLineParser`
 *                - `BorderedAsciiTableCommandLineParser`
 */
class AsciiTableCommandLineParserImpl(
                                       parsers: List[SpecializedAsciiTableCommandLineParser] = List(
                                         new CustomLinearAsciiTableCommandLineParser(),
                                         new DefaultLinearAsciiTableCommandLineParser(),
                                         new BourkeLinearAsciiTableCommandLineParser(),
                                         new BorderedAsciiTableCommandLineParser()
                                       )
                                     ) extends SingleInputCommandLineParser[AsciiConvertor](parsers) with AsciiTableCommandLineParser {

  override protected def noInputError(): BaseError = {
    BaseError(
      message = "You must specify a table type (--table=custom, --table=default, --table=bourke, or --table=bordered).",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }

  override protected def multipleInputError(): BaseError = {
    BaseError(
      message = "Only one table type can be specified (--table=custom, --table=default, --table=bourke, or --table=bordered).",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}
