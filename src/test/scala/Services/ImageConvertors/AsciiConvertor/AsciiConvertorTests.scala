package Services.ImageConvertors.AsciiConvertor

import Core.Models.AsciiTable.AsciiTable
import Core.Models.Image.{AsciiImage, GrayscaleImage}
import Core.Models.Pixel.{AsciiPixel, GrayscalePixel}
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.anyInt
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class AsciiConvertorTests extends AnyFunSuite with BeforeAndAfterEach {

  private val mockAsciiTable: AsciiTable = mock(classOf[AsciiTable])
  private var convertor: AsciiConvertor = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockAsciiTable.getAsciiCharacter(anyInt())).thenAnswer(invocation => {
      val intensity = invocation.getArgument(0).asInstanceOf[Int]
      if (intensity < 128) 'L'
      else 'D'
    })
    convertor = new AsciiConvertor(mockAsciiTable)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    reset(mockAsciiTable)
  }

  test("AsciiConvertor converts GrayscaleImage to AsciiImage with mocked AsciiTable") {
    val grayscaleImage = GrayscaleImage(Vector(
      Vector(GrayscalePixel(50), GrayscalePixel(200)),
      Vector(GrayscalePixel(120), GrayscalePixel(255))
    ))

    val asciiImage = convertor.convert(grayscaleImage)

    assert(asciiImage.getHeight == grayscaleImage.getHeight)
    assert(asciiImage.getWidth == grayscaleImage.getWidth)

    assert(asciiImage.pixels(0)(0).char == 'L')
    assert(asciiImage.pixels(0)(1).char == 'D')
    assert(asciiImage.pixels(1)(0).char == 'L')
    assert(asciiImage.pixels(1)(1).char == 'D')
  }
}
