package UI.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, ImageLoadingErrorCodes, LogContext}
import Services.Importers.FileImporters.*
import Services.Importers.Importer
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.FileImporterCommandLineParser
import org.mockito.ArgumentMatchers.any
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized
class FileImporterCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {
  private var gifImporterMock: MockedConstruction[GIFFileImporter] = uninitialized
  private var jpegImporterMock: MockedConstruction[JPEGFileImporter] = uninitialized
  private var pngImporterMock: MockedConstruction[PNGFileImporter] = uninitialized

  private val parser = new FileImporterCommandLineParser()

  override def beforeEach(): Unit = super.beforeEach()
  override def afterEach(): Unit = {
    super.afterEach()
  }

  test("No --image argument is given, returns None") {
    val args = Array("--other-command", "value")
    val result = parser.parse(args)
    assert(result == Right(None))
  }

  test("--image argument without filepath, returns InvalidArgument") {
    val args = Array("--image")
    val result = parser.parse(args)
    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Filepath was not specified"))
      case _ => fail("Expected Left(BaseError) but got something else.")
    }
  }
}