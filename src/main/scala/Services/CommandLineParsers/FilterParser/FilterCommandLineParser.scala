package Services.CommandLineParsers.FilterParser

import Services.CommandLineParsers.CommandLineParser
import Services.Filters.Filter
trait FilterCommandLineParser extends CommandLineParser[List[Filter]] {}