package Services.Importers.RandomImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes}
import Core.Models.Image.RGBImage
import Services.Importers.RandomImporters.RandomImporter
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.ArgumentMatchers._

import scala.compiletime.uninitialized
import scala.util.Random

class RandomImporterTests extends AnyFunSuite with BeforeAndAfterEach {
  val mockRandom: Random = mock(classOf[Random])
  var importer: RandomImporter = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    reset(mockRandom)
    importer = null
  }

  test("RandomImporter generates image with mocked random dimensions") {
    when(mockRandom.nextInt(anyInt())).thenReturn(100)

    importer = new RandomImporter(minWidth = 50, maxWidth = 200, minHeight = 50, maxHeight = 200, random = mockRandom)
    val image = importer.importImage()

    assert(image.getWidth == 150) // minWidth + mocked random (100)
    assert(image.getHeight == 150) // minHeight + mocked random (100)
  }

  test("RandomImporter generates image with exact dimensions") {
    importer = new RandomImporter(exactWidth = Some(100), exactHeight = Some(200))
    val image = importer.importImage()

    assert(image.getWidth == 100)
    assert(image.getHeight == 200)
  }

  test("RandomImporter throws BaseError for negative exact width") {
    val thrown = intercept[BaseError] {
      new RandomImporter(exactWidth = Some(-1))
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.InvalidImageDimensions)
  }

  test("RandomImporter throws BaseError for maxWidth < minWidth") {
    val thrown = intercept[BaseError] {
      new RandomImporter(minWidth = 100, maxWidth = 99)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.InvalidImageDimensions)
  }

  test("RandomImporter generates empty image when min/max dimensions are 0") {
    importer = new RandomImporter(minWidth = 0, maxWidth = 0, minHeight = 0, maxHeight = 0)
    val image = importer.importImage()

    assert(image.getWidth == 0)
    assert(image.getHeight == 0)
  }

  test("RandomImporter generates image with exact width and random height") {
    when(mockRandom.nextInt(anyInt())).thenReturn(50)

    importer = new RandomImporter(exactWidth = Some(150), minHeight = 50, maxHeight = 200, random = mockRandom)
    val image = importer.importImage()

    assert(image.getWidth == 150)
    assert(image.getHeight == 100) // minHeight + mocked random (50)
  }

  test("RandomImporter generates image with exact height and random width") {
    when(mockRandom.nextInt(anyInt())).thenReturn(20)

    importer = new RandomImporter(exactHeight = Some(100), minWidth = 30, maxWidth = 150, random = mockRandom)
    val image = importer.importImage()

    assert(image.getHeight == 100)
    assert(image.getWidth == 50) // minWidth + mocked random (20)
  }

  test("RandomImporter uses Random to generate RGB values") {
    when(mockRandom.nextInt(256)).thenReturn(100, 150, 200)

    importer = new RandomImporter(
      exactWidth = Some(1),
      exactHeight = Some(1),
      random = mockRandom
    )

    val image = importer.importImage()
    assert(image.getWidth == 1)
    assert(image.getHeight == 1)
    val pixel = image.pixels(0)(0)
    assert(pixel.red == 100)
    assert(pixel.green == 150)
    assert(pixel.blue == 200)
    verify(mockRandom, times(3)).nextInt(256) // R, G, B for one pixel
  }
}
