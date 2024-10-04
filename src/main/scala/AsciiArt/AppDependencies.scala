package AsciiArt

import Services.CommandLineParsing.{CommandLineParser, FilterFactory, LoaderFactory}
import Services.ImageLoader.ImageLoader
import Services.Logging.{ConsoleLoggerService, LoggerService}
import Services.Filters.Filter

object AppDependencies {
  lazy val logger: LoggerService = new ConsoleLoggerService()
  lazy val loaderFactory: CommandLineParser[ImageLoader] = LoaderFactory
  lazy val filterFactory: CommandLineParser[List[Filter]] = FilterFactory
}