package Services.ImageLoader
import java.io.File
import javax.imageio.ImageIO
import Core.Extensions.BufferedImageExtensions.*
import Core.Extensions.FileExtensions.*
import Core.Models.{Pixel, Image}

/**
 * A concrete implementation of ImageLoader that loads images from file paths.
 * Supports formats that ImageIO handles (e.g., JPG, PNG).
 */
class FileImageLoader(filePath: String) extends ImageLoader {

  /**
   * Loads an image from the provided file path.
   * If the format is unsupported or filepath is null, throws an exception.
   * @return an Image instance (either RGB, Grayscale, or ARGB).
   */
  override def loadImage(): Image[? <: Pixel] = {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null.")
    }
    val file = new File(filePath)
    file.validate()
    val bufferedImage = ImageIO.read(file)

    if (bufferedImage == null) {
      throw new IllegalArgumentException(s"Unsupported or invalid image format: $filePath")
    }

    bufferedImage.toImage
  }
}