package UI.CommandLineParsers.ExporterParser

import Services.Exporters.Exporter
import UI.CommandLineParsers.CommandLineParser

/**
 * A trait for parsing command line arguments to create an `Exporter`.
 *
 * This trait defines the generic interface for parsing command line arguments related
 * to output exporting. Implementing classes should handle specific export
 * types, such as file-based or console-based exports.
 */
trait ExporterCommandLineParser extends CommandLineParser[Exporter] {}
