package UI.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Importers.Importer
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.{FileImporterCommandLineParser, RandomImporterCommandLineParser, SpecializedImporterCommandLineParser}
import UI.CommandLineParsers.SingleInputCommandLineParser

/**
 * Parses command line arguments related to importing images.
 *
 * The `ImporterCommandLineParserImpl` class determines the appropriate image import method
 * based on the provided command line arguments. It delegates the parsing logic to a list
 * of specialized parsers for different import methods. This class ensures that exactly one
 * import method is selected, providing meaningful errors for invalid or conflicting inputs.
 *
 * Supported Import Methods:
 * - `--image <path>`: Specifies a file-based importer with the image located at `<path>`.
 * - `--image-random`: Specifies a random image generator importer.
 *
 * @param parsers A list of specialized parsers responsible for handling specific import methods.
 *                Defaults to the following parsers:
 *                - `FileImporterCommandLineParser`: Parses file-based import arguments.
 *                - `RandomImporterCommandLineParser`: Parses random image generation arguments.
 */
class ImporterCommandLineParserImpl(
                                     parsers: List[SpecializedImporterCommandLineParser[? <: Importer]] = List(
                                       new RandomImporterCommandLineParser(),
                                       new FileImporterCommandLineParser()
                                     )
                                   ) extends SingleInputCommandLineParser[Importer](parsers) with ImporterCommandLineParser {

  /**
   * Error to throw when no input is detected.
   */
  override protected def noInputError(): BaseError = {
    BaseError(
      message = "You must specify either --image or --image-random.",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }

  /**
   * Error to throw when multiple inputs are detected.
   */
  override protected def multipleInputError(): BaseError = {
    BaseError(
      message = "More than one import method detected, please select one method only.",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}