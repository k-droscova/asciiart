package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.FileImporterParsers

import Core.Errors.BaseError
import Services.Importers.Importer
import Services.Importers.FileImporters.PNGFileImporter
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.SpecializedImporterCommandLineParser

class PNGFileImporterCommandLineParser extends SpecializedImporterCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[Importer]] = {
    val imageArgIndex = args.indexOf("--image")
    if (imageArgIndex != -1 && imageArgIndex + 1 < args.length) {
      val filePath = args(imageArgIndex + 1).trim
      try {
        Right(Some(new PNGFileImporter(filePath)))
      } catch {
        case e: BaseError => Left(e)
      }
    } else {
      Right(None)
    }
  }
}
