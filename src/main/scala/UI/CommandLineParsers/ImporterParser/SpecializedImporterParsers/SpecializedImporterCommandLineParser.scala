package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers

import Core.Errors.BaseError
import Services.Importers.Importer
import UI.CommandLineParsers.CommandLineParser
trait SpecializedImporterCommandLineParser {
  /**
   * Attempts to parse the input arguments and return an `Either` representing
   * a successful `Importer` or a `BaseError` for validation failure.
   *
   * @param args The command line arguments to parse.
   * @return Either a `BaseError` (Left) or an `Option[Importer]` (Right).
   */
  def parse(args: Array[String]): Either[BaseError, Option[Importer]]
}