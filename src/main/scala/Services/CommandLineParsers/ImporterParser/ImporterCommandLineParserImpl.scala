package Services.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext, LogSeverity}
import Services.Importers.{FileImporter, RandomImporter}
import Services.Importers.Importer

class ImporterCommandLineParserImpl extends ImporterCommandLineParser {
  override def parse(input: String): Importer = {
    val args = input.split(" ").toList
    val (imagePath, randomImageRequested) = parseArguments(args)
    validateArguments(imagePath, randomImageRequested)
    createImporter(imagePath, randomImageRequested)
  }

  private def parseArguments(args: List[String]): (Option[String], Boolean) = {
    var imagePath: Option[String] = None
    var randomImageRequested = false

    for (i <- args.indices) {
      args(i) match {
        case "--image" =>
          if (imagePath.isDefined) {
            throw createBaseError("Only one --image argument is allowed.")
          }
          if (i + 1 < args.length) {
            val potentialPath = args(i + 1)
            if (potentialPath.startsWith("\"") && potentialPath.endsWith("\"")) {
              imagePath = Some(potentialPath.stripPrefix("\"").stripSuffix("\""))
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