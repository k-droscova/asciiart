package Services.ImageConvertors.GrayscaleConvertor

import Core.Models.Image.{GrayscaleImage, RGBImage}
import Core.Models.Pixel.{GrayscalePixel, RGBPixel}
import Services.ImageConvertors.GrayscaleConvertor.LinearGrayscaleConvertor
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class LinearGrayscaleConvertorTests extends AnyFunSuite with BeforeAndAfterEach {

  var convertor: LinearGrayscaleConvertor = uninitialized
  var rgbImage: RGBImage = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    convertor = new LinearGrayscaleConvertor
  }

  override def afterEach(): Unit = {
    super.afterEach()
    convertor = null
    rgbImage = null
  }

  test("convert single pixel to grayscale") {
    rgbImage = new RGBImage(Vector(
      Vector(new RGBPixel(255, 0, 0))
    ))
    val grayscaleImage = convertor.convert(rgbImage)
    assert(grayscaleImage.getPixel(0, 0).intensity == 76)
  }

  test("convert 2x2 RGB image to grayscale") {
    rgbImage = new RGBImage(Vector(
      Vector(new RGBPixel(255, 0, 0), new RGBPixel(0, 255, 0)),
      Vector(new RGBPixel(0, 0, 255), new RGBPixel(255, 255, 255))
    ))
    val grayscaleImage = convertor.convert(rgbImage)

    assert(grayscaleImage.getPixel(0, 0).intensity == 76)
    assert(grayscaleImage.getPixel(1, 0).intensity == 150)
    assert(grayscaleImage.getPixel(0, 1).intensity == 28)
    assert(grayscaleImage.getPixel(1, 1).intensity == 255)
    assert(grayscaleImage.getWidth == rgbImage.getWidth)
    assert(grayscaleImage.getHeight == rgbImage.getHeight)
  }

  test("convert 3x1 RGB image to grayscale and check dimensions") {
    rgbImage = new RGBImage(Vector(
      Vector(new RGBPixel(30, 60, 90), new RGBPixel(10, 20, 30), new RGBPixel(200, 100, 50))
    ))
    val grayscaleImage = convertor.convert(rgbImage)

    assert(grayscaleImage.getPixel(0, 0).intensity == 54)
    assert(grayscaleImage.getPixel(1, 0).intensity == 18)
    assert(grayscaleImage.getPixel(2, 0).intensity == 124)
    assert(grayscaleImage.getWidth == rgbImage.getWidth)
    assert(grayscaleImage.getHeight == rgbImage.getHeight)
  }

  test("convert empty image") {
    rgbImage = new RGBImage(Vector.empty)
    val grayscaleImage = convertor.convert(rgbImage)

    assert(grayscaleImage.getWidth == 0)
    assert(grayscaleImage.getHeight == 0)
  }

  test("convert grayscale value for edge RGB pixel values") {
    rgbImage = new RGBImage(Vector(
      Vector(new RGBPixel(0, 0, 0), new RGBPixel(255, 255, 255))
    ))
    val grayscaleImage = convertor.convert(rgbImage)

    assert(grayscaleImage.getPixel(0, 0).intensity == 0)
    assert(grayscaleImage.getPixel(1, 0).intensity == 255)
  }
}
