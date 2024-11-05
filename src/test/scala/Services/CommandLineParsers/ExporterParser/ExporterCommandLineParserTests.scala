package Services.CommandLineParsers.ExporterParser

import Core.Errors.{BaseError, GeneralErrorCodes}
import Services.Exporters.{FileExporter, ConsoleExporter}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized
import org.mockito.Mockito._
import org.mockito.MockedConstruction

class ExporterCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {
  private var parser: ExporterCommandLineParserImpl = uninitialized
  private var fileMock: MockedConstruction[FileExporter] = uninitialized
  private var consoleMock: MockedConstruction[ConsoleExporter] = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new ExporterCommandLineParserImpl()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("Valid input with single output file path") {
    fileMock = mockConstruction(classOf[FileExporter], (mocked, context) => {
      assert("path/to/output.txt" == context.arguments.get(0).asInstanceOf[String])
    })
    val exporter = parser.parse("--output-file \"path/to/output.txt\"")
    assert(exporter.isInstanceOf[FileExporter])
    fileMock.close()
  }

  test("Valid input with output to console") {
    consoleMock = mockConstruction(classOf[ConsoleExporter], (mocked, context) => {})
    val exporter = parser.parse("--output-console")
    assert(exporter.isInstanceOf[ConsoleExporter])
    consoleMock.close()
  }

  test("Mixed input with both output file and console") {
    val thrown = intercept[BaseError] {
      parser.parse("--output-file \"path/to/output.txt\" --output-console")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --output-file and --output-console."))
  }

  test("Multiple --output-file arguments") {
    val thrown = intercept[BaseError] {
      parser.parse("--output-file \"path/to/output.txt\" --output-file \"another/path/to/output.txt\"")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Only one --output-file argument is allowed."))
  }

  test("No arguments") {
    val thrown = intercept[BaseError] {
      parser.parse("")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify either --output-file or --output-console."))
  }

  test("Invalid flag") {
    val thrown = intercept[BaseError] {
      parser.parse("--invalidFlag")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify either --output-file or --output-console."))
  }

  test("Only --output-file without path") {
    val thrown = intercept[BaseError] {
      parser.parse("--output-file")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Output filepath was not specified after --output-file argument."))
  }

  test("--output-file with non-string path") {
    val thrown = intercept[BaseError] {
      parser.parse("--output-file NonStringPath")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Output filepath must be specified in quotes after --output-file argument."))
  }

  test("Whitespace handling") {
    val thrown = intercept[BaseError] {
      parser.parse("--output-file \"  path/to/output.txt  \"")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Output filepath must be specified in quotes after --output-file argument."))
  }

  test("Multiple arguments with both --output-file and --output-console") {
    val thrown = intercept[BaseError] {
      parser.parse("--rotate +90 --output-file \"path/to/output.txt\" --image-random --output-console")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --output-file and --output-console."))
  }
}
