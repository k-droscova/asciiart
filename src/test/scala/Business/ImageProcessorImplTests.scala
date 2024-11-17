package Business

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.Image.{AsciiImage, GrayscaleImage, RGBImage}
import Services.Exporters.Exporter
import Services.Filters.Filter
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import Services.ImageConvertors.GrayscaleConvertor.LinearGrayscaleConvertor
import Services.Importers.Importer
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class ImageProcessorImplTests extends AnyFunSuite with BeforeAndAfterEach {

  private val mockImporter: Importer = mock(classOf[Importer])
  private val mockGrayscaleConvertor: LinearGrayscaleConvertor = mock(classOf[LinearGrayscaleConvertor])
  private val mockFilter: Filter = mock(classOf[Filter])
  private val mockAsciiConvertor: AsciiConvertor = mock(classOf[AsciiConvertor])
  private val mockExporter: Exporter = mock(classOf[Exporter])

  private val mockRgbImage: RGBImage = mock(classOf[RGBImage])
  private val mockGrayscaleImage: GrayscaleImage = mock(classOf[GrayscaleImage])
  private val mockAsciiImage: AsciiImage = mock(classOf[AsciiImage])

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockImporter.importImage()).thenReturn(mockRgbImage)
    when(mockGrayscaleConvertor.convert(mockRgbImage)).thenReturn(mockGrayscaleImage)
    when(mockFilter.apply(mockGrayscaleImage)).thenReturn(mockGrayscaleImage)
    when(mockAsciiConvertor.convert(mockGrayscaleImage)).thenReturn(mockAsciiImage)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    reset(
      mockImporter,
      mockGrayscaleConvertor,
      mockFilter,
      mockAsciiConvertor,
      mockExporter
    )
  }

  test("ImageProcessorImpl processes and exports image correctly") {
    val processor = new ImageProcessorImpl(
      importer = mockImporter,
      filters = List(mockFilter),
      asciiConvertor = mockAsciiConvertor,
      exporter = mockExporter,
      grayscaleConvertor = mockGrayscaleConvertor
    )
    processor.processImage()

    verify(mockImporter).importImage()
    verify(mockGrayscaleConvertor).convert(mockRgbImage)
    verify(mockFilter).apply(mockGrayscaleImage)
    verify(mockAsciiConvertor).convert(mockGrayscaleImage)
    verify(mockExporter).exportImage(mockAsciiImage)
  }

  test("ImageProcessorImpl propagates error from importImage") {
    doAnswer(_ => throw BaseError(
      message = "Testing error in importImage",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )).when(mockImporter).importImage()

    val processor = new ImageProcessorImpl(
      importer = mockImporter,
      filters = List(mockFilter),
      asciiConvertor = mockAsciiConvertor,
      exporter = mockExporter,
      grayscaleConvertor = mockGrayscaleConvertor
    )

    val thrown = intercept[BaseError] {
      processor.processImage()
    }

    assert(thrown.message.contains("Testing error in importImage"))
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
  }

  test("ImageProcessorImpl propagates error from grayscaleConvertor") {
    doAnswer(_ => throw BaseError(
      message = "Testing error in convert to grayscale",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )).when(mockGrayscaleConvertor).convert(mockRgbImage)

    val processor = new ImageProcessorImpl(
      importer = mockImporter,
      filters = List(mockFilter),
      asciiConvertor = mockAsciiConvertor,
      exporter = mockExporter,
      grayscaleConvertor = mockGrayscaleConvertor
    )

    val thrown = intercept[BaseError] {
      processor.processImage()
    }

    assert(thrown.message.contains("Testing error in convert to grayscale"))
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
  }

  test("ImageProcessorImpl propagates error from filter's apply method") {
    doAnswer(_ => throw BaseError(
      message = "Testing error in filter apply",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )).when(mockFilter).apply(mockGrayscaleImage)

    val processor = new ImageProcessorImpl(
      importer = mockImporter,
      filters = List(mockFilter),
      asciiConvertor = mockAsciiConvertor,
      exporter = mockExporter,
      grayscaleConvertor = mockGrayscaleConvertor
    )

    val thrown = intercept[BaseError] {
      processor.processImage()
    }

    assert(thrown.message.contains("Testing error in filter apply"))
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
  }

  test("ImageProcessorImpl propagates error from asciiConvertor's convert method") {
    doAnswer(_ => throw BaseError(
      message = "Testing error in ASCII convert",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )).when(mockAsciiConvertor).convert(mockGrayscaleImage)

    val processor = new ImageProcessorImpl(
      importer = mockImporter,
      filters = List(mockFilter),
      asciiConvertor = mockAsciiConvertor,
      exporter = mockExporter,
      grayscaleConvertor = mockGrayscaleConvertor
    )

    val thrown = intercept[BaseError] {
      processor.processImage()
    }

    assert(thrown.message.contains("Testing error in ASCII convert"))
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
  }

  test("ImageProcessorImpl propagates error from exporter's exportImage method") {
    doAnswer(_ => throw BaseError(
      message = "Testing error in exportImage",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )).when(mockExporter).exportImage(mockAsciiImage)

    val processor = new ImageProcessorImpl(
      importer = mockImporter,
      filters = List(mockFilter),
      asciiConvertor = mockAsciiConvertor,
      exporter = mockExporter,
      grayscaleConvertor = mockGrayscaleConvertor
    )

    val thrown = intercept[BaseError] {
      processor.processImage()
    }

    assert(thrown.message.contains("Testing error in exportImage"))
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
  }
}
