package Services.Importers

import Core.Errors.{BaseError, ImageLoadingErrorCodes}
import Core.Models.Image.RGBImage
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized
class RandomImporterTests extends AnyFunSuite with BeforeAndAfterEach {
  var importer: RandomImporter = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    importer = null
  }

  test("RandomImporter should generate an image with random dimensions") {
    importer = new RandomImporter()
    val image = importer.importImage()
    assert(image.getWidth >= 1 && image.getWidth <= 800)
    assert(image.getHeight >= 1 && image.getHeight <= 800)
  }

  test("RandomImporter should generate an image with exact dimensions") {
    importer = new RandomImporter(exactWidth = Some(100), exactHeight = Some(200))
    val image = importer.importImage()
    assert(image.getWidth == 100)
    assert(image.getHeight == 200)
  }

  test("RandomImporter should generate an image within specified min and max dimensions") {
    importer = new RandomImporter(minWidth = 100, maxWidth = 200, minHeight = 150, maxHeight = 300)
    val image = importer.importImage()
    assert(image.getWidth >= 100 && image.getWidth <= 200)
    assert(image.getHeight >= 150 && image.getHeight <= 300)
  }

  test("RandomImporter should throw BaseError for negative exact width") {
    val thrown = intercept[BaseError] {
      importer = new RandomImporter(exactWidth = Some(-1))
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.InvalidImageDimensions)
  }

  test("RandomImporter should throw BaseError for negative exact height") {
    val thrown = intercept[BaseError] {
      importer = new RandomImporter(exactHeight = Some(-1))
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.InvalidImageDimensions)
  }

  test("RandomImporter should throw BaseError if maxWidth is less than minWidth") {
    val thrown = intercept[BaseError] {
      importer = new RandomImporter(minWidth = 100, maxWidth = 99)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.InvalidImageDimensions)
  }

  test("RandomImporter should throw BaseError if maxHeight is less than minHeight") {
    val thrown = intercept[BaseError] {
      importer = new RandomImporter(minHeight = 100, maxHeight = 99)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.InvalidImageDimensions)
  }

  test("RandomImporter should handle empty image generation (min/max width and min/max height = 0)") {
    importer = new RandomImporter(minWidth = 0, maxWidth = 0, minHeight = 0, maxHeight = 0)
    val image = importer.importImage()
    assert(image.getWidth == 0)
    assert(image.getHeight == 0)
  }

  test("RandomImporter should handle empty image generation (exact width and exact height = 0)") {
    importer = new RandomImporter(exactWidth = Some(0), exactHeight = Some(0))
    val image = importer.importImage()
    assert(image.getWidth == 0)
    assert(image.getHeight == 0)
  }

  test("RandomImporter should use exact width and use default min/max height") {
    val exactWidth = 150
    importer = new RandomImporter(exactWidth = Some(exactWidth))
    val image = importer.importImage()
    assert(image.getWidth == exactWidth)
    assert(image.getHeight >= 1 && image.getHeight <= 800)
  }

  test("RandomImporter should use exact height and use default min/max width") {
    val exactHeight = 250
    importer = new RandomImporter(exactHeight = Some(exactHeight))
    val image = importer.importImage()
    assert(image.getHeight == exactHeight)
    assert(image.getWidth >= 1 && image.getWidth <= 800)
  }

  test("RandomImporter should use exact width with min/max height") {
    val exactWidth = 200
    importer = new RandomImporter(exactWidth = Some(exactWidth), minHeight = 50, maxHeight = 200)
    val image = importer.importImage()
    assert(image.getWidth == exactWidth)
    assert(image.getHeight >= 50 && image.getHeight <= 200)
  }

  test("RandomImporter should use exact height with min/max width") {
    val exactHeight = 300
    importer = new RandomImporter(exactHeight = Some(exactHeight), minWidth = 50, maxWidth = 200)
    val image = importer.importImage()
    assert(image.getHeight == exactHeight)
    assert(image.getWidth >= 50 && image.getWidth <= 200)
  }

  test("RandomImporter creates empty image when min/max width is 0") {
    val exactHeight = 300
    importer = new RandomImporter(exactHeight = Some(exactHeight), minWidth = 0, maxWidth = 0)
    val image = importer.importImage()
    assert(image.getHeight == 0)
    assert(image.getWidth == 0)
  }

  test("RandomImporter creates empty image when min/max height is 0") {
    val exactWidth = 300
    importer = new RandomImporter(exactWidth = Some(exactWidth), minHeight = 0, maxHeight = 0)
    val image = importer.importImage()
    assert(image.getHeight == 0)
    assert(image.getWidth == 0)
  }
}
