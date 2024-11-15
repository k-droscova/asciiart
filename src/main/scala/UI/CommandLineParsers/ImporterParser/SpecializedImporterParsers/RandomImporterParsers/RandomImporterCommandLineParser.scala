package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.RandomImporterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Importers.Importer
import Services.Importers.RandomImporters.RandomImporter
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.SpecializedImporterCommandLineParser
class RandomImporterCommandLineParser extends SpecializedImporterCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[Importer]] = {
    val randomArgsCount = args.count(_ == "--image-random")

    if (randomArgsCount > 1) {
      Left(BaseError(
        message = "The --image-random argument should only occur once.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    } else if (randomArgsCount == 1) {
      Right(Some(new RandomImporter()))
    } else {
      Right(None)
    }
  }
}