package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers

import Core.Errors.BaseError
import Services.Importers.Importer
import UI.CommandLineParsers.SpecializedCommandLineParser

/**
 * A trait defining a parser for specialized command line arguments related to importing images.
 *
 * The parser is responsible for detecting its specific argument(s) in the input, validating them,
 * and returning the appropriate result.
 */
trait SpecializedImporterCommandLineParser[T <: Importer] extends SpecializedCommandLineParser[T]