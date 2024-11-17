package UI.CommandLineParsers.FilterParser

import Services.Filters.Filter
import UI.CommandLineParsers.CommandLineParser

/**
 * A trait for parsing command line arguments related to image filtering options.
 *
 * This trait defines an interface for creating a list of filters (`List[Filter]`) based
 * on command line input. Implementing classes should handle the logic for detecting,
 * validating, and creating filter instances based on the arguments provided.
 */
trait FilterCommandLineParser extends CommandLineParser[List[Filter]] {}