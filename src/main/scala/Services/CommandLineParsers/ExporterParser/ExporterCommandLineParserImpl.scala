package Services.CommandLineParsers.ExporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext, LogSeverity}
import Services.Exporters.Exporter
import Services.Exporters.{FileExporter, ConsoleExporter}
class ExporterCommandLineParserImpl extends ExporterCommandLineParser {
  override def parse(input: String): Exporter = {
    val args = input.split(" ").toList
    val (outputPath, outputToConsoleRequested) = parseArguments(args)
    validateArguments(outputPath, outputToConsoleRequested)
    createExporter(outputPath, outputToConsoleRequested)
  }

  private def parseArguments(args: List[String]): (Option[String], Boolean) = {
    var outputPath: Option[String] = None
    var outputToConsoleRequested = false
    val quotedPathPattern = "^\".*\"$".r

    for (i <- args.indices) {
      args(i) match {
        case "--output-file" =>
          if (outputPath.isDefined) {
            throw createBaseError("Only one --output-file argument is allowed.")
          }
          if (i + 1 < args.length) {
            val potentialPath = args(i + 1)
            if (quotedPathPattern.matches(potentialPath)) {
              outputPath = Some(potentialPath.stripPrefix("\"").stripSuffix("\""))
            } else {
              throw createBaseError("Output filepath must be specified in quotes after --output-file argument.")
            }
          } else {
            throw createBaseError("Output filepath was not specified after --output-file argument.")
          }

        case "--output-console" =>
          if (!outputToConsoleRequested) {
            outputToConsoleRequested = true
          } else {
            throw createBaseError("Only one --output-console argument is allowed.")
          }

        case _ =>
      }
    }
    (outputPath, outputToConsoleRequested)
  }

  private def validateArguments(outputPath: Option[String], outputToConsoleRequested: Boolean): Unit = {
    (outputPath, outputToConsoleRequested) match {
      case (Some(path), true) =>
        throw createBaseError("Cannot specify both --output-file and --output-console.")
      case (None, false) =>
        throw createBaseError("You must specify either --output-file or --output-console.")
      case _ =>
    }
  }

  private def createExporter(outputPath: Option[String], outputToConsoleRequested: Boolean): Exporter = {
    if (outputToConsoleRequested) {
      new ConsoleExporter()
    } else {
      outputPath match {
        case Some(path) => new FileExporter(path)
        case None => throw createBaseError("No output file argument specified.")
      }
    }
  }

  private def createBaseError(message: String): BaseError = {
    BaseError(
      message = message,
      severity = LogSeverity.Error,
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}
