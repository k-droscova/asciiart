package Core.Models.Image

import Core.Models.Pixel.RGBPixel

/**
 * Represents an RGB image composed of pixels of type `RGBPixel`.
 * Extends the generic `Image` class to provide functionality for handling
 * color image data in the RGB color space.
 *
 * @param pixels A two-dimensional Vector of `RGBPixel` objects, where each inner Vector represents a row.
 */
class RGBImage(pixels: Vector[Vector[RGBPixel]]) extends Image[RGBPixel](pixels) {}
