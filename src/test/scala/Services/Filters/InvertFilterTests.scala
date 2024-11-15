package Services.Filters

import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized
class InvertFilterTests extends AnyFunSuite with BeforeAndAfterEach {
  var grayscaleImage: GrayscaleImage = uninitialized
  var filter: InvertFilter = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    filter = new InvertFilter
  }

  override def afterEach(): Unit = {
    super.afterEach()
    grayscaleImage = null
    filter = null
  }

  test("InvertFilter should invert intensity values in a 2x2 image") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(100), new GrayscalePixel(150)),
      Vector(new GrayscalePixel(200), new GrayscalePixel(50))
    ))
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 155) // 255 - 100
    assert(resultImage.getPixel(1, 0).intensity == 105) // 255 - 150
    assert(resultImage.getPixel(0, 1).intensity == 55)  // 255 - 200
    assert(resultImage.getPixel(1, 1).intensity == 205) // 255 - 50
    assert(resultImage.getWidth == 2)
    assert(resultImage.getHeight == 2)
  }

  test("InvertFilter should handle an empty image") {
    grayscaleImage = new GrayscaleImage(Vector.empty)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getWidth == 0)
    assert(resultImage.getHeight == 0)
  }

  test("InvertFilter should handle a single-pixel image") {
    grayscaleImage = new GrayscaleImage(Vector(Vector(new GrayscalePixel(120))))
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 135) // 255 - 120
    assert(resultImage.getWidth == 1)
    assert(resultImage.getHeight == 1)
  }

  test("InvertFilter should correctly invert a 5x3 image and preserve dimensions") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(10), new GrayscalePixel(20), new GrayscalePixel(30), new GrayscalePixel(40), new GrayscalePixel(50)),
      Vector(new GrayscalePixel(60), new GrayscalePixel(70), new GrayscalePixel(80), new GrayscalePixel(90), new GrayscalePixel(100)),
      Vector(new GrayscalePixel(110), new GrayscalePixel(120), new GrayscalePixel(130), new GrayscalePixel(140), new GrayscalePixel(150))
    ))
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 245) // 255 - 10
    assert(resultImage.getPixel(1, 0).intensity == 235) // 255 - 20
    assert(resultImage.getPixel(2, 0).intensity == 225) // 255 - 30
    assert(resultImage.getPixel(3, 0).intensity == 215) // 255 - 40
    assert(resultImage.getPixel(4, 0).intensity == 205) // 255 - 50

    assert(resultImage.getPixel(0, 1).intensity == 195) // 255 - 60
    assert(resultImage.getPixel(1, 1).intensity == 185) // 255 - 70
    assert(resultImage.getPixel(2, 1).intensity == 175) // 255 - 80
    assert(resultImage.getPixel(3, 1).intensity == 165) // 255 - 90
    assert(resultImage.getPixel(4, 1).intensity == 155) // 255 - 100

    assert(resultImage.getPixel(0, 2).intensity == 145) // 255 - 110
    assert(resultImage.getPixel(1, 2).intensity == 135) // 255 - 120
    assert(resultImage.getPixel(2, 2).intensity == 125) // 255 - 130
    assert(resultImage.getPixel(3, 2).intensity == 115) // 255 - 140
    assert(resultImage.getPixel(4, 2).intensity == 105) // 255 - 150

    assert(resultImage.getWidth == 5)
    assert(resultImage.getHeight == 3)
  }
}
