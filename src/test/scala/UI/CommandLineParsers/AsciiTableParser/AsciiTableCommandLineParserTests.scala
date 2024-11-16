package UI.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import UI.CommandLineParsers.AsciiTableParser.AsciiTableCommandLineParserImpl
import UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers.{CustomLinearAsciiTableCommandLineParser, DefaultLinearAsciiTableCommandLineParser, BourkeLinearAsciiTableCommandLineParser, BorderedAsciiTableCommandLineParser}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class AsciiTableCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {

  private val customParser = mock(classOf[CustomLinearAsciiTableCommandLineParser])
  private val defaultParser = mock(classOf[DefaultLinearAsciiTableCommandLineParser])
  private val bourkeParser = mock(classOf[BourkeLinearAsciiTableCommandLineParser])
  private val borderedParser = mock(classOf[BorderedAsciiTableCommandLineParser])
  private val parserList = List(customParser, defaultParser, bourkeParser, borderedParser)
  private var parser: AsciiTableCommandLineParserImpl = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new AsciiTableCommandLineParserImpl(parserList)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
    reset(customParser, defaultParser, bourkeParser, borderedParser)
  }

  test("All parsers return None (no input provided)") {
    parserList.foreach(p => when(p.parse(any())).thenReturn(Right(None)))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify a table type (--custom-table, --table=default, --table=bourke, or --table=bordered)."))

    parserList.foreach(p => verify(p).parse(Array.empty))
  }

  test("First parser returns Some, others return None (valid custom table)") {
    when(customParser.parse(any())).thenReturn(Right(Some(mock(classOf[AsciiConvertor]))))
    parserList.tail.foreach(p => when(p.parse(any())).thenReturn(Right(None)))

    val result = parser.parse(Array("--custom-table", "custom-chars"))
    assert(result.isInstanceOf[AsciiConvertor])

    verify(customParser).parse(Array("--custom-table", "custom-chars"))
    parserList.tail.foreach(p => verify(p).parse(Array("--custom-table", "custom-chars")))
  }

  test("Only one parser returns Some (valid bordered table)") {
    when(borderedParser.parse(any())).thenReturn(Right(Some(mock(classOf[AsciiConvertor]))))
    parserList.filterNot(_ == borderedParser).foreach(p => when(p.parse(any())).thenReturn(Right(None)))

    val result = parser.parse(Array("--table=bordered", "custom-chars", "[1,2,3]"))
    assert(result.isInstanceOf[AsciiConvertor])

    verify(borderedParser).parse(Array("--table=bordered", "custom-chars", "[1,2,3]"))
    parserList.filterNot(_ == borderedParser).foreach(p => verify(p).parse(Array("--table=bordered", "custom-chars", "[1,2,3]")))
  }

  test("Multiple parsers return Some (conflicting inputs)") {
    when(customParser.parse(any())).thenReturn(Right(Some(mock(classOf[AsciiConvertor]))))
    when(defaultParser.parse(any())).thenReturn(Right(Some(mock(classOf[AsciiConvertor]))))

    val thrown = intercept[BaseError] {
      parser.parse(Array("--custom-table", "custom-chars", "--table=default"))
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Only one table type can be specified (--custom-table, --table=default, --table=bourke, or --table=bordered)."))

    verify(customParser).parse(Array("--custom-table", "custom-chars", "--table=default"))
    verify(defaultParser).parse(Array("--custom-table", "custom-chars", "--table=default"))
  }

  test("All parsers return errors") {
    when(customParser.parse(any())).thenReturn(Left(BaseError("Custom table error", LogContext.UI, GeneralErrorCodes.InvalidArgument)))
    when(defaultParser.parse(any())).thenReturn(Left(BaseError("Default table error", LogContext.UI, GeneralErrorCodes.InvalidArgument)))
    when(bourkeParser.parse(any())).thenReturn(Left(BaseError("Bourke table error", LogContext.UI, GeneralErrorCodes.InvalidArgument)))
    when(borderedParser.parse(any())).thenReturn(Left(BaseError("Bordered table error", LogContext.UI, GeneralErrorCodes.InvalidArgument)))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Only one table type can be specified (--custom-table, --table=default, --table=bourke, or --table=bordered)."))

    parserList.foreach(p => verify(p).parse(Array.empty))
  }

  test("One parser returns Some, another parser returns an error") {
    when(customParser.parse(any())).thenReturn(Left(BaseError("Custom table error", LogContext.UI, GeneralErrorCodes.InvalidArgument)))
    when(defaultParser.parse(any())).thenReturn(Right(Some(mock(classOf[AsciiConvertor]))))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Only one table type can be specified (--custom-table, --table=default, --table=bourke, or --table=bordered)."))

    verify(customParser).parse(Array.empty)
    verify(defaultParser).parse(Array.empty)
  }
}