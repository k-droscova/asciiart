package Services.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes}
import Services.Importers.{FileImporter, RandomImporter}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.any
import org.mockito.MockedConstruction
class ImporterCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {
  private var parser: ImporterCommandLineParserImpl = uninitialized
  private var path: String = uninitialized
  private var fileMock: MockedConstruction[FileImporter] = uninitialized
  private var randomMock: MockedConstruction[RandomImporter] = uninitialized
  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new ImporterCommandLineParserImpl()
    fileMock = mockConstruction(classOf[FileImporter], (mocked, context) => {})
    randomMock = mockConstruction(classOf[RandomImporter], (mocked, context) => {})
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
    path = null
    fileMock.close()
    randomMock.close()
  }

  test("Valid input with single image path") {
    val importer = parser.parse("--image \"path/to/image.jpg\"")
    assert(importer.isInstanceOf[FileImporter])
    fileMock.verify(_ (eq("path/to/image.jpg")), times(1))
  }

  test("Valid input with random image flag") {
    val importer = parser.parse("--image-random")
    assert(importer.isInstanceOf[RandomImporter])
  }

  test("Mixed input with both image and random") {
    val thrown = intercept[BaseError] {
      parser.parse("--image \"path/to/image.jpg\" --image-random")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --image and --image-random."))
  }

  test("Multiple --image arguments") {
    val thrown = intercept[BaseError] {
      parser.parse("--image \"path/to/image1.jpg\" --image \"path/to/image2.jpg\"")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Only one --image argument is allowed."))
  }

  test("No arguments") {
    val thrown = intercept[BaseError] {
      parser.parse("")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify either --image or --image-random."))
  }

  test("Invalid flag") {
    val thrown = intercept[BaseError] {
      parser.parse("--invalidFlag")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify either --image or --image-random."))
  }

  test("Only --image without path") {
    val thrown = intercept[BaseError] {
      parser.parse("--image")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Image filepath was not specified after --image argument."))
  }

  test("--image with non-string path") {
    val thrown = intercept[BaseError] {
      parser.parse("--image NonStringPath")
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Image filepath must be specified in quotes after --image argument."))
  }

  test("Whitespace handling") {
    val importer = parser.parse("--image \"  path/to/image.jpg  \"")
    assert(importer.isInstanceOf[FileImporter])
  }
}
