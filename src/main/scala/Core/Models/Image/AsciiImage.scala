package Core.Models.Image

import Core.Models.Pixel.AsciiPixel

/**
 * Represents an ASCII image composed of pixels of type `AsciiPixel`.
 * Extends the generic `Image` class to provide functionality for handling
 * ASCII-based image representations.
 *
 * @param pixels A two-dimensional Vector of `AsciiPixel` objects, where each inner Vector represents a row.
 */
class AsciiImage(pixels: Vector[Vector[AsciiPixel]]) extends Image[AsciiPixel](pixels) {}
