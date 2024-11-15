package Core.Models.Image

import Core.Errors.*
import Core.Models.Pixel.Pixel

/**
 * Abstract class representing an image composed of pixels arranged in rows and columns.
 * The image is represented as a two-dimensional Vector, where each inner Vector represents
 * a row of pixels. The class provides methods to access pixel data safely and retrieve
 * the dimensions of the image.
 *
 * @param pixels A two-dimensional Vector of pixels, where each inner Vector represents a row.
 *               The type of pixel (e.g., RGBPixel, GrayscalePixel) is specified by `P`, which
 *               is a subtype of `Pixel`.
 * @tparam P The type of pixel in the image, constrained to be a subtype of `Pixel`.
 */
abstract class Image[P <: Pixel](val pixels: Vector[Vector[P]]) {

  /**
   * The width of the image, calculated from the number of pixels in the first row.
   * If the image has no rows, width is set to 0.
   */
  private val width: Int = if (pixels.nonEmpty) pixels(0).size else 0

  /**
   * The height of the image, calculated from the number of rows in the `pixels` Vector.
   * If the image has empty rows, height is set to 0 regardless of how many rows there are.
   */
  private val height: Int = if (width > 0) pixels.size else 0

  /**
   * Retrieves the pixel at the specified (x, y) coordinates.
   *
   * @param x The horizontal position of the pixel (column index).
   * @param y The vertical position of the pixel (row index).
   * @return The pixel of type `P` at the specified coordinates.
   * @throws BaseError if the coordinates are out of bounds (e.g., x or y are negative,
   *                   or exceed the image dimensions).
   *
   * Example:
   * {{{
   *   val pixel = image.getPixel(1, 2) // Retrieves the pixel at row 2, column 1.
   * }}}
   */
  def getPixel(x: Int, y: Int): P = {
    if (x < 0 || y < 0 || y >= height || x >= width) {
      throw BaseError(message = s"Pixel coordinates ($x, $y) are out of bounds for image of size $width x $height.", context = LogContext.System, errorCode = GeneralErrorCodes.InvalidArgument)
    }
    pixels(y)(x)
  }

  /**
   * @return The width of the image (number of columns in each row).
   */
  def getWidth: Int = width

  /**
   * @return The height of the image (number of rows).
   */
  def getHeight: Int = height
}