package Services.ImageLoader

import Core.Models.{Pixel, Image}

/**
 * Trait for loading images.
 * Concrete implementations may load from files, generate random images, or load from other sources.
 */
trait ImageLoader {
  /**
   * Loads an image and returns it as an Image[P].
   * Implementations should provide logic for loading images from various sources.
   * @return a loaded Image.
   */
  def loadImage(): Image[? <: Pixel]
}