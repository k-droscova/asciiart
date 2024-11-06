package Services.CommandLineParsers.ExporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext, LogSeverity}
import Services.Exporters.Exporter
import Services.Exporters.{FileExporter, ConsoleExporter}

/**
 * The `ExporterCommandLineParserImpl` class is responsible for parsing command line arguments related to
 * output options for exporting ASCII art. It determines whether to output to a file or to the console based
 * on the provided arguments. This class implements the `ExporterCommandLineParser` trait.
 */
class ExporterCommandLineParserImpl extends ExporterCommandLineParser {

  /**
   * Parses the input string containing command line arguments and returns the corresponding Exporter.
   *
   * Supported arguments:
   * - `--output-file "path"`: Specifies the file path where the output will be saved. The path must be enclosed in quotes.
   * - `--output-console`: Specifies that the output should be printed to the console.
   *
   * @param input A string representing the command line arguments.
   * @return An instance of Exporter based on the parsed arguments.
   * @throws BaseError if:
   *   - More than one `--output-file` argument is provided.
   *   - The output file path is not specified after the `--output-file` argument.
   *   - More than one `--output-console` argument is specified.
   *   - Both `--output-file` and `--output-console` are specified together.
   *   - Neither `--output-file` nor `--output-console` is provided.
   */
  override def parse(args: Array[String]): Exporter = {
    val (outputPath, outputToConsoleRequested) = parseArguments(args)
    validateArguments(outputPath, outputToConsoleRequested)
    createExporter(outputPath, outputToConsoleRequested)
  }

  private def parseArguments(args: Array[String]): (Option[String], Boolean) = {
    var outputPath: Option[String] = None
    var outputToConsoleRequested = false

    for (i <- args.indices) {
      args(i) match {
        case "--output-file" =>
          if (outputPath.isDefined) {
            throw createBaseError("Only one --output-file argument is allowed.")
          }
          if (i + 1 < args.length) {
            outputPath = Some(args(i+1).trim)
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
