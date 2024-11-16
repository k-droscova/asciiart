package UI.CommandLineParsers.ExporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{ConsoleExporter, Exporter}
import UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers.ConsoleExporterCommandLineParser
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized

class ConsoleExporterCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {

  private var consoleExporterMock: MockedConstruction[ConsoleExporter] = uninitialized
  private var parser: ConsoleExporterCommandLineParser = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new ConsoleExporterCommandLineParser()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("Should return None if --output-console argument is not present") {
    val args = Array("--some-other-arg", "value")
    val result = parser.parse(args)
    assert(result == Right(None))
  }

  test("Should return ConsoleExporter if --output-console argument is present once") {
    consoleExporterMock = mockConstruction(classOf[ConsoleExporter], (_: ConsoleExporter, _: MockedConstruction.Context) => {})

    val args = Array("--output-console")
    val result = parser.parse(args)

    result match {
      case Right(Some(exporter)) =>
        assert(exporter == consoleExporterMock.constructed().get(0))
      case _ => fail("Expected Right(Some(ConsoleExporter)), but got something else.")
    }
    consoleExporterMock.close()
  }

  test("Should return error if --output-console argument is present multiple times") {
    val args = Array("--output-console", "--output-console")
    val result = parser.parse(args)
    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Only one --output-console argument is allowed."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }
}