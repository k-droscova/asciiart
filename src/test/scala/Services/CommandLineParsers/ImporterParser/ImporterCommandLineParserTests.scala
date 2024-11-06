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
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
    path = null
  }

  test("Valid input with single image path") {
    fileMock = mockConstruction(classOf[FileImporter], (mocked, context) => {
      assert("path/to/image.jpg" == context.arguments.get(0).asInstanceOf[String])
    })
    val importer = parser.parse(Array("--image", "path/to/image.jpg"))
    assert(importer.isInstanceOf[FileImporter])
    fileMock.close()
  }

  test("Whitespace handling") {
    fileMock = mockConstruction(classOf[FileImporter], (mocked, context) => {
      assert("path/to/image.jpg" == context.arguments.get(0).asInstanceOf[String])
    })
    val importer = parser.parse(Array("--image", "         path/to/image.jpg    "))
    assert(importer.isInstanceOf[FileImporter])
    fileMock.close()
  }

  test("Valid input with random image flag") {
    randomMock = mockConstruction(classOf[RandomImporter], (mocked, context) => {})
    val importer = parser.parse(Array("--image-random"))
    assert(importer.isInstanceOf[RandomImporter])
    randomMock.close()
  }

  test("Mixed input with both image and random") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--image", "path/to/image.jpg", "--image-random"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --image and --image-random."))
  }

  test("Multiple --image arguments") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--image", "path/to/image1.jpg", "--image", "path/to/image2.jpg"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Only one --image argument is allowed."))
  }

  test("No arguments") {
    val thrown = intercept[BaseError] {
      parser.parse(Array())
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify either --image or --image-random."))
  }

  test("Invalid flag") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--invalidFlag"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("You must specify either --image or --image-random."))
  }

  test("Only --image without path") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--image"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Image filepath was not specified after --image argument."))
  }

  test("Multiple arguments with both --image and --image-random") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--rotate", "+90", "--image-random", "--scale", "0.5", "--invert", "--image", "path/to/image.jpg"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Cannot specify both --image and --image-random."))
  }
}
