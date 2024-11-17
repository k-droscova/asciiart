package Services.Filters

import Core.Errors.{BaseError, FilterErrorCodes}
import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized
class RotateFilterTests extends AnyFunSuite with BeforeAndAfterEach {
  var grayscaleImage: GrayscaleImage = uninitialized
  var filter: RotateFilter = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    grayscaleImage = null
    filter = null
  }

  test("RotateFilter should throw BaseError for angle not a multiple of 90") {
    val thrown = intercept[BaseError] {
      filter = new RotateFilter(45)
    }
    assert(thrown.errorCode == FilterErrorCodes.InvalidRotationAngle)
    assert(thrown.message.contains("Angle 45 is invalid. Angle must be a multiple of 90 degrees."))
  }

  test("RotateFilter should rotate 90 degrees clockwise and preserve dimensions in 2x2 image") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2)),
      Vector(new GrayscalePixel(3), new GrayscalePixel(4))
    ))
    filter = new RotateFilter(90)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 3)
    assert(resultImage.getPixel(1, 0).intensity == 1)
    assert(resultImage.getPixel(0, 1).intensity == 4)
    assert(resultImage.getPixel(1, 1).intensity == 2)
    assert(resultImage.getWidth == 2)
    assert(resultImage.getHeight == 2)
  }

  test("RotateFilter should handle 90 degrees rotation in 2x3 image and adjust dimensions") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2)),
      Vector(new GrayscalePixel(3), new GrayscalePixel(4)),
      Vector(new GrayscalePixel(5), new GrayscalePixel(6))
    ))
    filter = new RotateFilter(90)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 5)
    assert(resultImage.getPixel(1, 0).intensity == 3)
    assert(resultImage.getPixel(2, 0).intensity == 1)
    assert(resultImage.getPixel(0, 1).intensity == 6)
    assert(resultImage.getPixel(1, 1).intensity == 4)
    assert(resultImage.getPixel(2, 1).intensity == 2)
    assert(resultImage.getWidth == grayscaleImage.getHeight)
    assert(resultImage.getHeight == grayscaleImage.getWidth)
  }

  test("RotateFilter should rotate 180 degrees and preserve dimensions in 3x5 image") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2), new GrayscalePixel(3)),
      Vector(new GrayscalePixel(4), new GrayscalePixel(5), new GrayscalePixel(6)),
      Vector(new GrayscalePixel(7), new GrayscalePixel(8), new GrayscalePixel(9)),
      Vector(new GrayscalePixel(10), new GrayscalePixel(11), new GrayscalePixel(12)),
      Vector(new GrayscalePixel(13), new GrayscalePixel(14), new GrayscalePixel(15))
    ))
    filter = new RotateFilter(180)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 15)
    assert(resultImage.getPixel(1, 0).intensity == 14)
    assert(resultImage.getPixel(2, 0).intensity == 13)
    assert(resultImage.getPixel(0, 1).intensity == 12)
    assert(resultImage.getPixel(1, 1).intensity == 11)
    assert(resultImage.getPixel(2, 1).intensity == 10)
    assert(resultImage.getPixel(0, 2).intensity == 9)
    assert(resultImage.getPixel(1, 2).intensity == 8)
    assert(resultImage.getPixel(2, 2).intensity == 7)
    assert(resultImage.getPixel(0, 3).intensity == 6)
    assert(resultImage.getPixel(1, 3).intensity == 5)
    assert(resultImage.getPixel(2, 3).intensity == 4)
    assert(resultImage.getPixel(0, 4).intensity == 3)
    assert(resultImage.getPixel(1, 4).intensity == 2)
    assert(resultImage.getPixel(2, 4).intensity == 1)

    assert(resultImage.getWidth == grayscaleImage.getWidth)
    assert(resultImage.getHeight == grayscaleImage.getHeight)
  }

  test("RotateFilter should rotate 270 degrees clockwise and adjust dimensions in 2x3 image") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2)),
      Vector(new GrayscalePixel(3), new GrayscalePixel(4)),
      Vector(new GrayscalePixel(5), new GrayscalePixel(6))
    ))
    filter = new RotateFilter(270)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 2)
    assert(resultImage.getPixel(1, 0).intensity == 4)
    assert(resultImage.getPixel(2, 0).intensity == 6)
    assert(resultImage.getPixel(0, 1).intensity == 1)
    assert(resultImage.getPixel(1, 1).intensity == 3)
    assert(resultImage.getPixel(2, 1).intensity == 5)

    assert(resultImage.getWidth == grayscaleImage.getHeight)
    assert(resultImage.getHeight == grayscaleImage.getWidth)
  }

  test("RotateFilter should return original image for 0 degrees rotation") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2)),
      Vector(new GrayscalePixel(3), new GrayscalePixel(4))
    ))
    filter = new RotateFilter(0)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 1)
    assert(resultImage.getPixel(1, 0).intensity == 2)
    assert(resultImage.getPixel(0, 1).intensity == 3)
    assert(resultImage.getPixel(1, 1).intensity == 4)
    assert(resultImage.getWidth == grayscaleImage.getWidth)
    assert(resultImage.getHeight == grayscaleImage.getHeight)
  }

  test("RotateFilter should rotate -90 degrees and adjust dimensions in 2x3 image") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2)),
      Vector(new GrayscalePixel(3), new GrayscalePixel(4)),
      Vector(new GrayscalePixel(5), new GrayscalePixel(6))
    ))
    filter = new RotateFilter(-90)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 2)
    assert(resultImage.getPixel(1, 0).intensity == 4)
    assert(resultImage.getPixel(2, 0).intensity == 6)
    assert(resultImage.getPixel(0, 1).intensity == 1)
    assert(resultImage.getPixel(1, 1).intensity == 3)
    assert(resultImage.getPixel(2, 1).intensity == 5)
    assert(resultImage.getWidth == grayscaleImage.getHeight)
    assert(resultImage.getHeight == grayscaleImage.getWidth)
  }

  test("RotateFilter should rotate -180 degrees and preserve dimensions in 3x5 image") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2), new GrayscalePixel(3)),
      Vector(new GrayscalePixel(4), new GrayscalePixel(5), new GrayscalePixel(6)),
      Vector(new GrayscalePixel(7), new GrayscalePixel(8), new GrayscalePixel(9)),
      Vector(new GrayscalePixel(10), new GrayscalePixel(11), new GrayscalePixel(12)),
      Vector(new GrayscalePixel(13), new GrayscalePixel(14), new GrayscalePixel(15))
    ))
    filter = new RotateFilter(-180)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 15)
    assert(resultImage.getPixel(1, 0).intensity == 14)
    assert(resultImage.getPixel(2, 0).intensity == 13)
    assert(resultImage.getPixel(0, 1).intensity == 12)
    assert(resultImage.getPixel(1, 1).intensity == 11)
    assert(resultImage.getPixel(2, 1).intensity == 10)
    assert(resultImage.getPixel(0, 2).intensity == 9)
    assert(resultImage.getPixel(1, 2).intensity == 8)
    assert(resultImage.getPixel(2, 2).intensity == 7)
    assert(resultImage.getPixel(0, 3).intensity == 6)
    assert(resultImage.getPixel(1, 3).intensity == 5)
    assert(resultImage.getPixel(2, 3).intensity == 4)
    assert(resultImage.getPixel(0, 4).intensity == 3)
    assert(resultImage.getPixel(1, 4).intensity == 2)
    assert(resultImage.getPixel(2, 4).intensity == 1)

    assert(resultImage.getWidth == grayscaleImage.getWidth)
    assert(resultImage.getHeight == grayscaleImage.getHeight)
  }

  test("RotateFilter should rotate -270 degrees and adjust dimensions in 2x3 image") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2)),
      Vector(new GrayscalePixel(3), new GrayscalePixel(4)),
      Vector(new GrayscalePixel(5), new GrayscalePixel(6))
    ))
    filter = new RotateFilter(-270)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getPixel(0, 0).intensity == 5)
    assert(resultImage.getPixel(1, 0).intensity == 3)
    assert(resultImage.getPixel(2, 0).intensity == 1)
    assert(resultImage.getPixel(0, 1).intensity == 6)
    assert(resultImage.getPixel(1, 1).intensity == 4)
    assert(resultImage.getPixel(2, 1).intensity == 2)
    assert(resultImage.getWidth == grayscaleImage.getHeight)
    assert(resultImage.getHeight == grayscaleImage.getWidth)
  }

  test("RotateFilter should return equivalent results for multiples of 360 degrees plus 0, 90, 180, and 270") {
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(1), new GrayscalePixel(2), new GrayscalePixel(3)),
      Vector(new GrayscalePixel(4), new GrayscalePixel(5), new GrayscalePixel(6)),
      Vector(new GrayscalePixel(7), new GrayscalePixel(8), new GrayscalePixel(9))
    ))
    val angles = List(0, 90, 180, 270)
    val multiplesOf360 = (4 to 8).map(_ * 360)

    angles.foreach { baseAngle =>
      val expectedImage = new RotateFilter(baseAngle).apply(grayscaleImage)

      multiplesOf360.foreach { multiple =>
        val totalAngle = baseAngle + multiple
        filter = new RotateFilter(totalAngle)
        val resultImage = filter.apply(grayscaleImage)

        assert(resultImage.getWidth == expectedImage.getWidth)
        assert(resultImage.getHeight == expectedImage.getHeight)

        for (y <- 0 until resultImage.getHeight) {
          for (x <- 0 until resultImage.getWidth) {
            assert(resultImage.getPixel(x, y).intensity == expectedImage.getPixel(x, y).intensity,
              s"Failed for rotation angle $totalAngle degrees at position ($x, $y)")
          }
        }
      }
    }
  }

  test("RotateFilter should handle an empty image") {
    grayscaleImage = new GrayscaleImage(Vector.empty)
    filter = new RotateFilter(90)
    val resultImage = filter.apply(grayscaleImage)

    assert(resultImage.getWidth == 0)
    assert(resultImage.getHeight == 0)
  }

  test("RotateFilter should handle a single-pixel image and keep it unchanged") {
    grayscaleImage = new GrayscaleImage(Vector(Vector(new GrayscalePixel(42))))
    val angles = List(0, 90, 180, 270, 360, -90, -180, -270)

    angles.foreach { angle =>
      filter = new RotateFilter(angle)
      val resultImage = filter.apply(grayscaleImage)
      assert(resultImage.getWidth == 1)
      assert(resultImage.getHeight == 1)
      assert(resultImage.getPixel(0, 0).intensity == 42, s"Failed for rotation angle $angle")
    }
  }
}
