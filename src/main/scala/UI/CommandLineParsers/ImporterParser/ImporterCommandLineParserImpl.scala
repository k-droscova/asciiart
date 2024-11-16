package UI.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Importers.Importer
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.FileImporterParsers.FileImporterCommandLineParser
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.RandomImporterParsers.RandomImporterCommandLineParser
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.SpecializedImporterCommandLineParser
import UI.CommandLineParsers.SingleInputCommandLineParser

/**
 * Parses command line arguments related to importing images.
 *
 * This class implements the `ImporterCommandLineParser` trait, providing functionality
 * to parse user input and return the appropriate `Importer` instance based on the arguments.
 */
class ImporterCommandLineParserImpl(
                                     parsers: List[SpecializedImporterCommandLineParser] = List(
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