package Core.Models.Image

import Core.Models.Pixel.AsciiPixel
class AsciiImage(pixels: Vector[Vector[AsciiPixel]]) extends Image[AsciiPixel](pixels) {}
