package UI.CommandLineParsers.FilterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Filters.*
import UI.CommandLineParsers.FilterParser.SpecializedFilterParsers.{BrightnessFilterCommandLineParser, InvertFilterCommandLineParser, RotateFilterCommandLineParser, SpecializedFilterCommandLineParser}

/**
 * Implementation of `FilterCommandLineParser` that supports multiple image filtering options.
 *
 * The `FilterCommandLineParserImpl` delegates the parsing logic to a list of specialized parsers,
 * each responsible for detecting and validating a specific filter type. It aggregates all parsed
 * filters into a single list and ensures that invalid or conflicting filters are not created.
 *
 * Supported Filters:
 * - `--rotate <angle>`: Rotates the image by the specified angle (must be divisible by 90).
 * - `--invert`: Applies the invert filter to the image.
 * - `--brightness <value>`: Adjusts the image brightness by the specified value (positive or negative).
 *
 * @param parsers A list of specialized parsers for the supported filter types. Defaults to the following parsers:
 *                - `RotateFilterCommandLineParser`: Handles the `--rotate` argument.
 *                - `InvertFilterCommandLineParser`: Handles the `--invert` argument.
 *                - `BrightnessFilterCommandLineParser`: Handles the `--brightness` argument.
 */
class FilterCommandLineParserImpl(
                                   parsers: List[SpecializedFilterCommandLineParser[? <: Filter]] = List(
                                     new RotateFilterCommandLineParser(),
                                     new InvertFilterCommandLineParser(),
                                     new BrightnessFilterCommandLineParser()
                                   )
                                 ) extends FilterCommandLineParser {

  /**
   * Parses the provided command line arguments to create a list of filters.
   *
   * @param args An array of command line arguments.
   * @return A list of `Filter` instances based on the parsed arguments.
   * @throws BaseError if any of the specialized parsers encounter invalid input or conflicting arguments.
   */
  override def parse(args: Array[String]): List[Filter] = {
    parsers.flatMap { parser =>
      parser.parse(args) match {
        case Left(error)          => throw error // Throw the error if parsing fails
        case Right(Some(filters)) => filters.toList // Return the list of filters from the parser
        case Right(None)          => List.empty // Return an empty list if the parser doesn't find filters
      }
    }
  }
}