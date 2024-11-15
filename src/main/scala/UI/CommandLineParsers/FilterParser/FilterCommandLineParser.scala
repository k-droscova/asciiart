package UI.CommandLineParsers.FilterParser

import Services.Filters.Filter
import UI.CommandLineParsers.CommandLineParser

trait FilterCommandLineParser extends CommandLineParser[List[Filter]] {}