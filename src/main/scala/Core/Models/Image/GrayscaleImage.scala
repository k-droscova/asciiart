package Core.Models.Image

import Core.Models.Pixel.GrayscalePixel

class GrayscaleImage(pixels: Vector[Vector[GrayscalePixel]]) extends Image[GrayscalePixel](pixels) {}

