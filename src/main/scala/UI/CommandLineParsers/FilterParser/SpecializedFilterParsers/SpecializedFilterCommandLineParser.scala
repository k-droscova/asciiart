package UI.CommandLineParsers.FilterParser.SpecializedFilterParsers

import Services.Filters.Filter
import UI.CommandLineParsers.SpecializedCommandLineParser

/**
 * A specialized parser for extracting filter-related arguments from the command line.
 *
 * Each implementation is responsible for detecting and validating its specific argument(s),
 * and producing a list of corresponding `Filter` instances.
 *
 * @tparam T The type of filter this parser handles.
 */
trait SpecializedFilterCommandLineParser[T <: Filter] extends SpecializedCommandLineParser[List[T]]