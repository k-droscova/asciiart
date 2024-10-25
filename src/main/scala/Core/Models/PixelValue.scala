package Core.Models

trait PixelValue {}

case class RGBPixelValue(red: Int, green: Int, blue: Int) extends PixelValue {}

case class GreyscalePixelValue(intensity: Int) extends PixelValue {}

case class AlphaPixelValue(red: Int, green: Int, blue: Int, alpha: Int) extends PixelValue {}
