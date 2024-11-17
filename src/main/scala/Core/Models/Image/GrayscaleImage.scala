package Core.Models.Image

import Core.Models.Pixel.GrayscalePixel

/**
 * Represents a grayscale image composed of pixels of type `GrayscalePixel`.
 * Extends the generic `Image` class to provide functionality for handling
 * grayscale image data.
 *
 * @param pixels A two-dimensional Vector of `GrayscalePixel` objects, where each inner Vector represents a row.
 */
class GrayscaleImage(pixels: Vector[Vector[GrayscalePixel]]) extends Image[GrayscalePixel](pixels) {}

