package UI

import Business.{ImageProcessor, ImageProcessorImpl}
import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.Exporter
import Services.Filters.Filter
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import Services.Importers.Importer
import UI.CommandLineParsers.AsciiTableParser.{AsciiTableCommandLineParser, AsciiTableCommandLineParserImpl}
import UI.CommandLineParsers.ExporterParser.{ExporterCommandLineParser, ExporterCommandLineParserImpl}
import UI.CommandLineParsers.FilterParser.{FilterCommandLineParser, FilterCommandLineParserImpl}
import UI.CommandLineParsers.ImporterParser.{ImporterCommandLineParser, ImporterCommandLineParserImpl}
import org.mockito.ArgumentMatchers.any
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class ConsoleViewModelTests extends AnyFunSuite with BeforeAndAfterEach {
  private val mockAsciiTableParser: AsciiTableCommandLineParser = mock(classOf[AsciiTableCommandLineParser])
  private val mockImportParser: ImporterCommandLineParser = mock(classOf[ImporterCommandLineParser])
  private val mockExportParser: ExporterCommandLineParser = mock(classOf[ExporterCommandLineParser])
  private val mockFilterParser: FilterCommandLineParser = mock(classOf[FilterCommandLineParser])

  private var imageProcessorMock: MockedConstruction[ImageProcessorImpl] = uninitialized

  private val mockImporter: Importer = mock(classOf[Importer])
  private val mockAsciiConvertor: AsciiConvertor = mock(classOf[AsciiConvertor])
  private val mockExporter: Exporter = mock(classOf[Exporter])
  private val mockFilter: Filter = mock(classOf[Filter])

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockImportParser.parse(any())).thenReturn(mockImporter)
    when(mockAsciiTableParser.parse(any())).thenReturn(mockAsciiConvertor)
    when(mockFilterParser.parse(any())).thenReturn(List(mockFilter))
    when(mockExportParser.parse(any())).thenReturn(mockExporter)
  }
  override def afterEach(): Unit = {
    super.afterEach()
    reset(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser,
      mockImporter,
      mockAsciiConvertor,
      mockExporter,
      mockFilter
    )
  }

  test("ConsoleViewModel.run calls dependencies in correct sequence and passes correct arguments") {
    imageProcessorMock = mockConstruction(classOf[ImageProcessorImpl], (mocked, context) => {
      assert(context.arguments.get(0) == mockImporter)
      assert(context.arguments.get(1) == List(mockFilter))
      assert(context.arguments.get(2) == mockAsciiConvertor)
      assert(context.arguments.get(3) == mockExporter)
    })
    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser
    )

    val args = Array("some", "test", "arguments")
    viewModel.run(args)

    verify(mockImportParser).parse(args)
    verify(mockAsciiTableParser).parse(args)
    verify(mockFilterParser).parse(args)
    verify(mockExportParser).parse(args)

    assert(imageProcessorMock.constructed().size() == 1)
    verify(imageProcessorMock.constructed().get(0)).processImage()
    imageProcessorMock.close()
  }

  test("ConsoleViewModel.run propagates error from processImage") {
    imageProcessorMock = mockConstruction(classOf[ImageProcessorImpl], (mocked, _) => {
      doAnswer(_ => throw BaseError("Testing error in processImage", LogContext.UI, GeneralErrorCodes.UnknownError))
        .when(mocked)
        .processImage()
    })
    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.message.contains("Testing error in processImage"))
    assert(thrown.errorCode == GeneralErrorCodes.UnknownError)
    imageProcessorMock.close()
  }

  test("ConsoleViewModel.run propagates error from importParser") {
    doAnswer(_ => throw BaseError("Testing error", LogContext.UI, GeneralErrorCodes.InvalidArgument)).when(mockImportParser).parse(any())

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser
    )

    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error"))
  }

  test("ConsoleViewModel.run propagates error from exportParser") {
    doAnswer(_ => throw BaseError("Testing error in exportParser", LogContext.UI, GeneralErrorCodes.InvalidArgument)).when(mockExportParser).parse(any())

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in exportParser"))
  }

  test("ConsoleViewModel.run propagates error from filterParser") {
    doAnswer(_ => throw BaseError("Testing error in filterParser", LogContext.UI, GeneralErrorCodes.InvalidArgument)).when(mockFilterParser).parse(any())

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in filterParser"))
  }

  test("ConsoleViewModel.run propagates error from asciiTableParser") {
    doAnswer(_ => throw BaseError("Testing error in asciiTableParser", LogContext.UI, GeneralErrorCodes.InvalidArgument)).when(mockAsciiTableParser).parse(any())

    val viewModel = new ConsoleViewModel(
      mockAsciiTableParser,
      mockImportParser,
      mockExportParser,
      mockFilterParser
    )
    val args = Array("some", "test", "arguments")
    val thrown = intercept[BaseError] {
      viewModel.run(args)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Testing error in asciiTableParser"))
  }
}