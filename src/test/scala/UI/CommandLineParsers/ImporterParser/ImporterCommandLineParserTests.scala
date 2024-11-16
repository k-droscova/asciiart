package UI.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, ImageLoadingErrorCodes, LogContext}
import Services.Importers.FileImporters.FileImporter
import Services.Importers.RandomImporters.RandomImporter
import Services.Importers.Importer
import UI.CommandLineParsers.ImporterParser.ImporterCommandLineParserImpl
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.{FileImporterCommandLineParser, RandomImporterCommandLineParser, SpecializedImporterCommandLineParser}
import org.mockito.ArgumentMatchers.any
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class ImporterCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {
  private val randomImporterParser: SpecializedImporterCommandLineParser[RandomImporter] = mock(classOf[RandomImporterCommandLineParser])
  private val fileImporterParser: SpecializedImporterCommandLineParser[FileImporter] = mock(classOf[FileImporterCommandLineParser])
  private val parserList: List[SpecializedImporterCommandLineParser[? <: Importer]] = List(randomImporterParser, fileImporterParser)
  private var parser: ImporterCommandLineParserImpl = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new ImporterCommandLineParserImpl(parserList)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
    reset(randomImporterParser, fileImporterParser)
  }

  test("Both parsers return None (no input provided)") {
    when(randomImporterParser.parse(any())).thenReturn(Right(None))
    when(fileImporterParser.parse(any())).thenReturn(Right(None))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify either --image or --image-random."))

    verify(randomImporterParser).parse(Array.empty)
    verify(fileImporterParser).parse(Array.empty)
  }

  test("First parser returns Some, second parser returns None (valid random importer)") {
    when(randomImporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[RandomImporter]))))
    when(fileImporterParser.parse(any())).thenReturn(Right(None))

    val importer = parser.parse(Array("--image-random"))
    assert(importer.isInstanceOf[RandomImporter])

    verify(randomImporterParser).parse(Array("--image-random"))
    verify(fileImporterParser).parse(Array("--image-random"))
  }

  test("First parser returns None, second parser returns Some (valid file importer)") {
    when(randomImporterParser.parse(any())).thenReturn(Right(None))
    when(fileImporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[FileImporter]))))

    val importer = parser.parse(Array("--image", "path/to/image.jpg"))
    assert(importer.isInstanceOf[FileImporter])

    verify(randomImporterParser).parse(Array("--image", "path/to/image.jpg"))
    verify(fileImporterParser).parse(Array("--image", "path/to/image.jpg"))
  }

  test("Both parsers return Some (conflicting inputs)") {
    when(randomImporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[RandomImporter]))))
    when(fileImporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[FileImporter]))))

    val thrown = intercept[BaseError] {
      parser.parse(Array("--image", "path/to/image.jpg", "--image-random"))
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("More than one import method detected, please select one method only."))
  }

  test("Both parsers return errors, propagates the first error") {
    when(randomImporterParser.parse(any())).thenReturn(Left(BaseError("random error", LogContext.UI, GeneralErrorCodes.InvalidArgument)))
    when(fileImporterParser.parse(any())).thenReturn(Left(BaseError("other random error", LogContext.UI, ImageLoadingErrorCodes.FileNotFound)))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("More than one import method detected, please select one method only."))
  }

  test("First parser returns Some, second parser returns error (valid random importer)") {
    when(randomImporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[RandomImporter]))))
    when(fileImporterParser.parse(any())).thenReturn(Left(BaseError("Invalid file format", LogContext.UI, ImageLoadingErrorCodes.UnsupportedFormat)))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("More than one import method detected, please select one method only."))
  }

  // CASE 6: One parser returns Left, the other returns Some
  test("First parser returns error, second parser returns Some (valid file importer)") {
    when(randomImporterParser.parse(any())).thenReturn(Left(BaseError("Invalid random input", LogContext.UI, GeneralErrorCodes.InvalidArgument)))
    when(fileImporterParser.parse(any())).thenReturn(Right(Some(mock(classOf[FileImporter]))))

    val thrown = intercept[BaseError] {
      parser.parse(Array.empty)
    }

    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("More than one import method detected, please select one method only."))
  }
}