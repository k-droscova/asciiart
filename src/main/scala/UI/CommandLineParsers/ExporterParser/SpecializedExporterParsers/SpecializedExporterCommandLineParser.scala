package UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers

import Services.Exporters.Exporter
import UI.CommandLineParsers.SpecializedCommandLineParser

/**
 * A trait for specialized parsers that handle export-related command line arguments.
 *
 * The `SpecializedExporterCommandLineParser` is responsible for detecting and validating
 * arguments specific to a particular type of `Exporter`. Implementing classes should
 * produce a specific subtype of `Exporter` based on the parsed arguments.
 *
 * @tparam T The type of `Exporter` produced by the parser.
 */
trait SpecializedExporterCommandLineParser[T <: Exporter] extends SpecializedCommandLineParser[T]