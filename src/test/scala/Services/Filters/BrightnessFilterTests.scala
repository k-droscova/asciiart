package Services.Filters

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import scala.compiletime.uninitialized

import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel

class BrightnessFilterTests extends AnyFunSuite with BeforeAndAfterEach {
  var grayscaleImage: GrayscaleImage = uninitialized
  var filter: BrightnessFilter = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    grayscaleImage = null
    filter = null
  }

  test("BrightnessFilter should increase brightness by specified amount in 1x2 image") {
    val originalImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(100)),
      Vector(new GrayscalePixel(150))
    ))
    val filter = new BrightnessFilter(20)
    val resultImage = filter.apply(originalImage)

    assert(resultImage.getPixel(0, 0).intensity == 120)
    assert(resultImage.getPixel(0, 1).intensity == 170)
    assert(resultImage.getWidth == 1)
    assert(resultImage.getHeight == 2)
  }

  test("BrightnessFilter should decrease brightness by specified amount in 1x2 image") {
    val originalImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(100)),
      Vector(new GrayscalePixel(150))
    ))
    val filter = new BrightnessFilter(-30)
    val resultImage = filter.apply(originalImage)

    assert(resultImage.getPixel(0, 0).intensity == 70)
    assert(resultImage.getPixel(0, 1).intensity == 120)
    assert(resultImage.getWidth == 1)
    assert(resultImage.getHeight == 2)
  }

  test("BrightnessFilter should increase brightness in a 3x3 image and preserve dimensions") {
    val originalImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(10), new GrayscalePixel(20), new GrayscalePixel(30)),
      Vector(new GrayscalePixel(40), new GrayscalePixel(50), new GrayscalePixel(60)),
      Vector(new GrayscalePixel(70), new GrayscalePixel(80), new GrayscalePixel(90))
    ))
    val filter = new BrightnessFilter(10)
    val resultImage = filter.apply(originalImage)

    assert(resultImage.getPixel(0, 0).intensity == 20)
    assert(resultImage.getPixel(1, 1).intensity == 60)
    assert(resultImage.getPixel(2, 2).intensity == 100)
    assert(resultImage.getWidth == 3)
    assert(resultImage.getHeight == 3)
  }

  test("BrightnessFilter should clamp intensity values to 255 in a 2x3 image") {
    val originalImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(240), new GrayscalePixel(250)),
      Vector(new GrayscalePixel(200), new GrayscalePixel(210)),
      Vector(new GrayscalePixel(230), new GrayscalePixel(245))
    ))
    val filter = new BrightnessFilter(20)
    val resultImage = filter.apply(originalImage)

    assert(resultImage.getPixel(0, 0).intensity == 255)
    assert(resultImage.getPixel(1, 0).intensity == 255)
    assert(resultImage.getPixel(0, 1).intensity == 220)
    assert(resultImage.getPixel(1, 1).intensity == 230)
    assert(resultImage.getPixel(0, 2).intensity == 250)
    assert(resultImage.getPixel(1, 2).intensity == 255)
    assert(resultImage.getWidth == 2)
    assert(resultImage.getHeight == 3)
  }

  test("BrightnessFilter should clamp intensity values to 0 in a 4x1 image") {
    val originalImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(10), new GrayscalePixel(5), new GrayscalePixel(3), new GrayscalePixel(7))
    ))
    val filter = new BrightnessFilter(-20)
    val resultImage = filter.apply(originalImage)

    assert(resultImage.getPixel(0, 0).intensity == 0)
    assert(resultImage.getPixel(1, 0).intensity == 0)
    assert(resultImage.getPixel(2, 0).intensity == 0)
    assert(resultImage.getPixel(3, 0).intensity == 0)
    assert(resultImage.getWidth == 4)
    assert(resultImage.getHeight == 1)
  }

  test("BrightnessFilter should handle an empty image") {
    grayscaleImage = new GrayscaleImage(Vector.empty)
    filter = new BrightnessFilter(50)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getWidth == 0)
    assert(resultImage.getHeight == 0)
  }
}