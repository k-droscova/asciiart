package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, ImageLoadingErrorCodes, LogContext}
import Services.Importers.FileImporters.*
import Services.Importers.Importer
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.SpecializedImporterCommandLineParser

import scala.util.{Try, boundary}
import scala.util.boundary.break

class FileImporterCommandLineParser(
                                     val importers: List[String => FileImporter] = List(
                                       path => new GIFFileImporter(path),
                                       path => new JPEGFileImporter(path),
                                       path => new PNGFileImporter(path)
                                     )
                                   ) extends SpecializedImporterCommandLineParser {

  override def parse(args: Array[String]): Either[BaseError, Option[Importer]] = {
    val fileArgs = args.sliding(2).filter(_.head == "--image").toList

    if (fileArgs.size > 1) {
      return Left(BaseError(
        message = "Only one --image argument is allowed.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    if (fileArgs.isEmpty) {
      return Right(None) // No file argument, return None
    }

    val path = fileArgs.head.last
    if (path.startsWith("--")) {
      return Left(BaseError(
        message = "Filepath was not specified after --image.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    constructImportersOneByOne(importers, path.trim).getOrElse(
      Left(BaseError(
        message = "Unable to read the file, ensure the file is in supported format.",
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.UnsupportedFormat
      ))
    )
  }

  private def constructImportersOneByOne(importers: List[String => FileImporter], filePath: String): Option[Either[BaseError, Option[Importer]]] = {
    println("constructing one by one")
    boundary[Option[Either[BaseError, Option[Importer]]]]:
      for (importerFactory <- importers) {
        try {
          println("constructing " + importerFactory.toString())
          val importer = importerFactory(filePath) // Attempt to create importer
          break(Some(Right(Some(importer)))) // Success, exit immediately
        } catch {
          case e: BaseError if shouldExitEarly(e) =>
            break(Some(Left(e))) // Exit early for specific errors
          case t: Throwable =>
            break(Some(Left(BaseError(
              message = s"Unexpected error: ${t.getMessage}",
              context = LogContext.System,
              errorCode = GeneralErrorCodes.UnknownError
            ))))
        }
      }
      None // If no importer succeeds, return None
  }
  private def shouldExitEarly(error: BaseError): Boolean = {
    println("checking for early exit for " + error.errorCode)
    error.errorCode match {
      case ImageLoadingErrorCodes.FileNotFound | ImageLoadingErrorCodes.FileUnreadable => true
      case _ => false
    }
  }
}