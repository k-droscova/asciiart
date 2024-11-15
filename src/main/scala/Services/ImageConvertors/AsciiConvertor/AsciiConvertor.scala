package Services.ImageConvertors.AsciiConvertor

import Core.Models.AsciiTable.AsciiTable
import Core.Models.Image.{AsciiImage, GrayscaleImage}
import Core.Models.Pixel.AsciiPixel
import Services.ImageConvertors.ImageConvertor


/**
 * Class for converting a GrayscaleImage to an AsciiImage using a specified AsciiTable.
 *
 * @param asciiTable The table that maps grayscale intensities to ASCII characters.
 */
class AsciiConvertor(asciiTable: AsciiTable) extends ImageConvertor[GrayscaleImage, AsciiImage] {

  /**
   * Converts a GrayscaleImage to an AsciiImage by mapping each pixel's intensity to an ASCII character.
   *
   * @param input The GrayscaleImage to convert.
   * @return The resulting AsciiImage.
   */
  override def convert(input: GrayscaleImage): AsciiImage = {
    val asciiPixels = input.pixels.map { row =>
      row.map { pixel =>
        val asciiChar = asciiTable.getAsciiCharacter(pixel.intensity)
        AsciiPixel(asciiChar)
      }
    }
    AsciiImage(asciiPixels)
  }
}
