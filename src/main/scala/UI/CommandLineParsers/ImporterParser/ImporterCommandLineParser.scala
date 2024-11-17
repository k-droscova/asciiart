package UI.CommandLineParsers.ImporterParser

import Services.Importers.Importer
import UI.CommandLineParsers.CommandLineParser

/**
 * A trait for parsing command line arguments to determine the image import method.
 *
 * The `ImporterCommandLineParser` trait defines the interface for parsers that handle
 * import-related arguments, such as selecting between file-based imports and random
 * image generation. Implementations of this trait are responsible for producing an
 * appropriate `Importer` instance based on the parsed arguments.
 */
trait ImporterCommandLineParser extends CommandLineParser[Importer] {}
