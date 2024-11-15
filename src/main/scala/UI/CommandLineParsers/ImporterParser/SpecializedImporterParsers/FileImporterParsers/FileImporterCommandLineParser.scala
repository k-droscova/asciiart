package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.FileImporterParsers

import Core.Errors.{BaseError, ImageLoadingErrorCodes, LogContext}
import Services.Importers.Importer
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.SpecializedImporterCommandLineParser

class FileImporterCommandLineParser(
                                     val gifParser: SpecializedImporterCommandLineParser = new GIFFileImporterCommandLineParser(),
                                     val jpegParser: SpecializedImporterCommandLineParser = new JPEGFileImporterCommandLineParser(),
                                     val pngParser: SpecializedImporterCommandLineParser = new PNGFileImporterCommandLineParser()
                                   ) extends SpecializedImporterCommandLineParser {

  override def parse(args: Array[String]): Either[BaseError, Option[Importer]] = {
    // Attempt to parse using each specialized parser
    val results = List(
      gifParser.parse(args),
      jpegParser.parse(args),
      pngParser.parse(args)
    )

    // Separate successful parsers and errors
    val successfulParsers = results.collect { case Right(Some(importer)) => importer }
    val errors = results.collect { case Left(error) => error }

    // Validate results
    if (successfulParsers.size > 1) {
      Left(BaseError(
        message = "Multiple importers matched the provided arguments. Please specify only one supported format.",
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.InvalidImageDimensions
      ))
    } else if (successfulParsers.isEmpty && errors.nonEmpty) {
      // If no successful parsers and errors exist, return the first error
      Left(errors.head)
    } else {
      // Return the single successful parser or None
      Right(successfulParsers.headOption)
    }
  }
}
