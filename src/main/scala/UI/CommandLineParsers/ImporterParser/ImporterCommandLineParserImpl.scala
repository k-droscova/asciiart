package UI.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Importers.Importer
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.FileImporterParsers.FileImporterCommandLineParser
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.RandomImporterParsers.RandomImporterCommandLineParser
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.SpecializedImporterCommandLineParser

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
                                   ) extends ImporterCommandLineParser {

  /**
   * Parses the input string containing command line arguments and returns the corresponding `Importer`.
   *
   * @param args A string array representing the command line arguments.
   * @return An instance of `Importer` based on the parsed arguments.
   * @throws BaseError if the input is invalid, if required arguments are missing, or if conflicting
   *                   options are provided.
   */
  override def parse(args: Array[String]): Importer = {
    val results = parsers.map(_.parse(args))

    val successfulParsers = results.collect { case Right(Some(importer)) => importer }
    val errors = results.collect { case Left(error) => error }

    validateParsers(successfulParsers, errors)
  }

  private def validateParsers(successfulParsers: List[Importer], errors: List[BaseError]): Importer = {
    val totalInputs = successfulParsers.size + errors.size

    totalInputs match {
      case 0 => // No input was provided
        throw BaseError(
          message = "You must specify either --image or --image-random.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        )

      case 1 => // Exactly one input was provided
        successfulParsers.headOption match {
          case Some(importer) => importer // Successfully parsed, return the importer
          case None => throw errors.head // Input failed validation, propagate the error
        }

      case _ => // More than one input method detected
        throw BaseError(
          message = "More than one import method detected, please select one method only.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        )
    }
  }
}