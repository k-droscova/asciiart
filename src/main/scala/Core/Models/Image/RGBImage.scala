package Core.Models.Image

import Core.Models.Pixel.RGBPixel
class RGBImage(pixels: Vector[Vector[RGBPixel]]) extends Image[RGBPixel](pixels) {}
