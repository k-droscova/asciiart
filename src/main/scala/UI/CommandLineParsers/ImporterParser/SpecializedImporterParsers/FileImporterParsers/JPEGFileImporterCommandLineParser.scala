package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.FileImporterParsers

import Services.Importers.FileImporters.JPEGFileImporter
import Core.Errors.BaseError
import Services.Importers.Importer
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.SpecializedImporterCommandLineParser
class JPEGFileImporterCommandLineParser extends SpecializedImporterCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[Importer]] = {
    val imageArgIndex = args.indexOf("--image")
    if (imageArgIndex != -1 && imageArgIndex + 1 < args.length) {
      val filePath = args(imageArgIndex + 1).trim
      try {
        Right(Some(new JPEGFileImporter(filePath)))
      } catch {
        case e: BaseError => Left(e) // Propagate the validation error
      }
    } else {
      Right(None) // Not responsible for this case
    }
  }
}
