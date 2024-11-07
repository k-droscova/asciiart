package UI

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext, LogSeverity}
import Core.Models.Image.{AsciiImage, GrayscaleImage, RGBImage}
import Services.CommandLineParsers.AsciiTableParser.AsciiTableCommandLineParserImpl
import Services.CommandLineParsers.ExporterParser.ExporterCommandLineParserImpl
import Services.CommandLineParsers.FilterParser.FilterCommandLineParserImpl
import Services.CommandLineParsers.ImporterParser.ImporterCommandLineParserImpl
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import Services.ImageConvertors.GrayscaleConvertor.LinearGrayscaleConvertor
import Services.Importers.Importer
import Services.Exporters.Exporter
import Services.Filters.Filter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized
import org.mockito.Mockito.*
import org.mockito.MockedConstruction
import org.mockito.ArgumentMatchers.any

class ConsoleViewModelTests extends AnyFunSuite with BeforeAndAfterEach {
  private val mockAsciiTableParser: AsciiTableCommandLineParserImpl = mock(classOf[AsciiTableCommandLineParserImpl])
  private val mockImportParser: ImporterCommandLineParserImpl = mock(classOf[ImporterCommandLineParserImpl])
  private val mockExportParser: ExporterCommandLineParserImpl = mock(classOf[ExporterCommandLineParserImpl])
  private val mockFilterParser: FilterCommandLineParserImpl = mock(classOf[FilterCommandLineParserImpl])
  private val mockGrayscaleConvertor: LinearGrayscaleConvertor = mock(classOf[LinearGrayscaleConvertor])

  private val mockImporter: Importer = mock(classOf[Importer])
  private val mockAsciiConvertor: AsciiConvertor = mock(classOf[AsciiConvertor])
  private val mockExporter: Exporter = mock(classOf[Exporter])
  private val mockFilter: Filter = mock(classOf[Filter])
  private val mockRgbImage: RGBImage = mock(classOf[RGBImage])
  private val mockGrayscaleImage: GrayscaleImage = mock(classOf[GrayscaleImage])
  private val mockAsciiImage: AsciiImage = mock(classOf[AsciiImage])

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockImportParser.parse(any())).thenReturn(mockImporter)
    when(mockAsciiTableParser.parse(any())).thenReturn(mockAsciiConvertor)
    when(mockFilterParser.parse(any())).thenReturn(List(mockFilter))
    when(mockExportParser.parse(any())).thenReturn(mockExporter)

    when(mockImporter.importImage()).thenReturn(mockRgbImage)
    when(mockGrayscaleConvertor.convert(mockRgbImage)).thenReturn(mockGrayscaleImage)
    when(mockFilter.apply(mockGrayscaleImage)).thenReturn(mockGrayscaleImage)
    when(mockAsciiConvertor.convert(mockGrayscaleImage)).thenReturn(mockAsciiImage)
  }
  override def afterEach(): Unit = {
    super.afterEach()
    reset(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor,
      mockImporter,
      mockAsciiConvertor,
      mockExporter,
      mockFilter,
      mockRgbImage,
      mockGrayscaleImage,
      mockAsciiImage
    )
  }

  test("ConsoleViewModel.run calls dependencies in correct sequence and passes correct arguments") {
    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )

    val args = Array("some", "test", "arguments")
    viewModel.run(args)

    verify(mockImportParser).parse(args)
    verify(mockAsciiTableParser).parse(args)
    verify(mockFilterParser).parse(args)
    verify(mockExportParser).parse(args)

    verify(mockImporter).importImage()
    verify(mockGrayscaleConvertor).convert(mockRgbImage)
    verify(mockFilter).apply(mockGrayscaleImage)
    verify(mockAsciiConvertor).convert(mockGrayscaleImage)
    verify(mockExporter).exportImage(mockAsciiImage)
  }

  test("ConsoleViewModel.run propagates error from importParser") {
    doAnswer(_ => throw BaseError(
      "Testing error",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockImportParser).parse(any())

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error"))
  }

  test("ConsoleViewModel.run propagates error from exportParser") {
    doAnswer(_ => throw BaseError(
      "Testing error in exportParser",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockExportParser).parse(any())

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in exportParser"))
  }

  test("ConsoleViewModel.run propagates error from filterParser") {
    doAnswer(_ => throw BaseError(
      "Testing error in filterParser",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockFilterParser).parse(any())

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in filterParser"))
  }

  test("ConsoleViewModel.run propagates error from asciiTableParser") {
    doAnswer(_ => throw BaseError(
      "Testing error in asciiTableParser",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockAsciiTableParser).parse(any())

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in asciiTableParser"))
  }

  test("ConsoleViewModel.run propagates error from importer's importImage") {
    doAnswer(_ => throw BaseError(
      "Testing error in importImage",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockImporter).importImage()

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in importImage"))
  }

  test("ConsoleViewModel.run propagates error from grayscaleConvertor's convert method") {
    doAnswer(_ => throw BaseError(
      "Testing error in convert to grayscale",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockGrayscaleConvertor).convert(mockRgbImage)

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in convert to grayscale"))
  }

  test("ConsoleViewModel.run propagates error from filter's apply method") {
    doAnswer(_ => throw BaseError(
      "Testing error in filter apply",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockFilter).apply(mockGrayscaleImage)

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in filter apply"))
  }

  test("ConsoleViewModel.run propagates error from asciiConvertor's convert method") {
    doAnswer(_ => throw BaseError(
      "Testing error in ASCII convert",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockAsciiConvertor).convert(mockGrayscaleImage)

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in ASCII convert"))
  }

  test("ConsoleViewModel.run propagates error from exporter's exportImage method") {
    doAnswer(_ => throw BaseError(
      "Testing error in exportImage",
      LogSeverity.Error,
      LogContext.UI,
      GeneralErrorCodes.InvalidArgument
    )).when(mockExporter).exportImage(mockAsciiImage)

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockGrayscaleConvertor
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in exportImage"))
  }
}