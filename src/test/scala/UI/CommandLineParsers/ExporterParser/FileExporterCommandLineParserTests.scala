package UI.CommandLineParsers.ExporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{Exporter, FileExporter}
import Services.Importers.FileImporters.PNGFileImporter
import UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers.FileExporterCommandLineParser
import org.mockito.Mockito.*
import org.mockito.Mockito
import org.mockito.MockedConstruction
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized
class FileExporterCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {

  private var fileExporterMock: MockedConstruction[FileExporter] = uninitialized
  private var parser: FileExporterCommandLineParser = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new FileExporterCommandLineParser()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("Should return None if --output-file argument is not present") {
    val args = Array("--some-other-arg", "value")
    val result = parser.parse(args)
    assert(result == Right(None))
  }

  test("Should return FileExporter if --output-file argument is present with a valid path") {
    fileExporterMock = mockConstruction(classOf[FileExporter], (mocked, context) => {
      assert("output.txt" == context.arguments.get(0).asInstanceOf[String])
    })

    val args = Array("--output-file", "output.txt")
    val result = parser.parse(args)

    result match {
      case Right(Some(exporter)) => assert(exporter == fileExporterMock.constructed().get(0))
      case _ => fail("Expected Right(Some(FileExporter)), but got something else.")
    }
    fileExporterMock.close()
  }

  test("Should return error if --output-file argument is present multiple times") {
    val args = Array("--output-file", "output1.txt", "--output-file", "output2.txt")
    val result = parser.parse(args)
    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Only one --output-file argument is allowed."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }

  test("Should return error if --output-file argument is not followed by a path") {
    val args = Array("--output-file", "--another-arg")
    val result = parser.parse(args)
    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Output filepath was not specified after --output-file."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }
}