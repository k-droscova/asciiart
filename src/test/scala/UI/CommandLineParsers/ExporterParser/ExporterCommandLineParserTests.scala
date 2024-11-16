package UI.CommandLineParsers.ExporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{ConsoleExporter, Exporter, FileExporter}
import UI.CommandLineParsers.ExporterParser.ExporterCommandLineParserImpl
import UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers.*
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.any
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class ExporterCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {
  private val fileExporterParser: SpecializedExporterCommandLineParser[FileExporter] = mock(classOf[FileExporterCommandLineParser])
  private val consoleExporterParser: SpecializedExporterCommandLineParser[ConsoleExporter] = mock(classOf[ConsoleExporterCommandLineParser])
  private val parserList: List[SpecializedExporterCommandLineParser[? <: Exporter]] = List(fileExporterParser, consoleExporterParser)
  private var parser: ExporterCommandLineParserImpl = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new ExporterCommandLineParserImpl(parserList)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
    reset(fileExporterParser, consoleExporterParser)
  }

  test("Both parsers return None (no input provided)") {
    when(fileExporterParser.parse(any())).thenReturn(Right(None))
    when(consoleExporterParser.parse(any())).thenReturn(Right(None))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify either --output-file or --output-console."))
  }

  test("First parser returns Some, second parser returns None (valid file exporter)") {
    when(fileExporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[FileExporter]))))
    when(consoleExporterParser.parse(any())).thenReturn(Right(None))

    val exporter = parser.parse(Array("--output-file", "output.txt"))
    assert(exporter.isInstanceOf[FileExporter])

    verify(fileExporterParser).parse(Array("--output-file", "output.txt"))
    verify(consoleExporterParser).parse(Array("--output-file", "output.txt"))
  }

  test("First parser returns None, second parser returns Some (valid console exporter)") {
    when(fileExporterParser.parse(any())).thenReturn(Right(None))
    when(consoleExporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[ConsoleExporter]))))

    val exporter = parser.parse(Array("--output-console"))
    assert(exporter.isInstanceOf[ConsoleExporter])

    verify(fileExporterParser).parse(Array("--output-console"))
    verify(consoleExporterParser).parse(Array("--output-console"))
  }

  test("Both parsers return Some (conflicting inputs)") {
    when(fileExporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[FileExporter]))))
    when(consoleExporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[ConsoleExporter]))))

    val thrown = intercept[BaseError] {
      parser.parse(Array("--output-file", "output.txt", "--output-console"))
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --output-file and --output-console."))
  }

  test("Both parsers return errors") {
    when(fileExporterParser.parse(any())).thenReturn(Left(BaseError("File error", LogContext.UI, GeneralErrorCodes.InvalidArgument)))
    when(consoleExporterParser.parse(any())).thenReturn(Left(BaseError("Console error", LogContext.UI, GeneralErrorCodes.InvalidArgument)))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --output-file and --output-console."))
  }

  test("First parser returns Some, second parser returns error") {
    when(fileExporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[FileExporter]))))
    when(consoleExporterParser.parse(any())).thenReturn(Left(BaseError("Invalid console argument", LogContext.UI, GeneralErrorCodes.InvalidArgument)))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --output-file and --output-console."))
  }

  test("First parser returns error, second parser returns Some") {
    when(fileExporterParser.parse(any())).thenReturn(Left(BaseError("Invalid file argument", LogContext.UI, GeneralErrorCodes.InvalidArgument)))
    when(consoleExporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[ConsoleExporter]))))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --output-file and --output-console."))
  }
}