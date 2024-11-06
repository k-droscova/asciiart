package Services.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext, LogSeverity}
import Services.Importers.{FileImporter, RandomImporter}
import Services.Importers.Importer

/**
 * Parses command line arguments related to importing images.
 *
 * This class implements the `ImporterCommandLineParser` trait, providing functionality
 * to parse user input and return the appropriate `Importer` instance based on the arguments.
 *
 * Supported command line arguments:
 * - `--image "<path>"`: Specifies the path to the image file. The path must be enclosed in quotes.
 * - `--image-random`: Indicates that a random image should be used instead of a specified file.
 *
 * If both `--image` and `--image-random` are provided, or if no valid input is given, an error will be thrown.
 */
class ImporterCommandLineParserImpl extends ImporterCommandLineParser {
  /**
   * Parses the input string containing command line arguments and returns the corresponding `Importer`.
   *
   * @param input A string representing the command line arguments. The input can include options for
   *              specifying an image path or requesting a random image. Example input:
   *              "--image \"path/to/image.jpg\""
   * @return An instance of `Importer` based on the parsed arguments. This could be either a `FileImporter`
   *         for the specified path or a `RandomImporter` if the random image argument is used.
   * @throws BaseError if the input is invalid, if required arguments are missing, or if conflicting
   *                   options are provided. Specific cases include:
   *                   - Missing path for `--image`
   *                   - Attempting to specify both `--image` and `--image-random`
   *                   - Invalid path format not enclosed in quotes for `--image`
   */
  override def parse(input: String): Importer = {
    val args = splitArguments(input)
    val (imagePath, randomImageRequested) = parseArguments(args)
    validateArguments(imagePath, randomImageRequested)
    createImporter(imagePath, randomImageRequested)
  }

  private def parseArguments(args: List[String]): (Option[String], Boolean) = {
    var imagePath: Option[String] = None
    var randomImageRequested = false
    val quotedPathPattern = "^\".*\"$".r

    for (i <- args.indices) {
      args(i) match {
        case "--image" =>
          if (imagePath.isDefined) {
            throw createBaseError("Only one --image argument is allowed.")
          }
          if (i + 1 < args.length) {
            val potentialPath = args(i + 1)
            if (quotedPathPattern.matches(potentialPath)) {
              imagePath = Some(extractQuotedInputAndTrim(potentialPath))
            } else {
              throw createBaseError("Image filepath must be specified in quotes after --image argument.")
            }
          } else {
            throw createBaseError("Image filepath was not specified after --image argument.")
          }

        case "--image-random" =>
          if (!randomImageRequested) {
            randomImageRequested = true
          } else {
            throw createBaseError("Only one --image-random argument is allowed.")
          }

        case _ =>
      }
    }
    (imagePath, randomImageRequested)
  }

  private def validateArguments(imagePath: Option[String], randomImageRequested: Boolean): Unit = {
    (imagePath, randomImageRequested) match {
      case (Some(path), true) =>
        throw createBaseError("Cannot specify both --image and --image-random.")

      case (None, false) =>
        throw createBaseError("You must specify either --image or --image-random.")

      case _ =>
    }
  }

  private def createImporter(imagePath: Option[String], randomImageRequested: Boolean): Importer = {
    if (randomImageRequested) {
      RandomImporter()
    } else {
      imagePath match {
        case Some(path) => new FileImporter(path)
        case None => throw createBaseError("No image argument specified.")
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