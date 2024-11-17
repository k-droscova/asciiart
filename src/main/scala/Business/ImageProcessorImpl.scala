package Business

import Core.Models.Image.{AsciiImage, GrayscaleImage, RGBImage}
import Services.Exporters.Exporter
import Services.Filters.Filter
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import Services.ImageConvertors.GrayscaleConvertor.{GrayscaleConvertor, LinearGrayscaleConvertor}
import Services.Importers.Importer

/**
 * Implementation of the ImageProcessor trait.
 * Orchestrates the full image processing pipeline: import -> grayscale conversion -> filtering -> ASCII conversion -> export.
 *
 * @param importer           The importer used to read the source image.
 * @param filters            A list of filters to be applied to the grayscale image.
 * @param asciiConvertor     The converter for transforming the processed grayscale image into ASCII art.
 * @param exporter           The exporter used to save or display the ASCII art.
 * @param grayscaleConvertor The converter for transforming the RGB image to grayscale (default: LinearGrayscaleConvertor).
 */
class ImageProcessorImpl(
                          private val importer: Importer,
                          private val filters: List[Filter],
                          private val asciiConvertor: AsciiConvertor,
                          private val exporter: Exporter,
                          private val grayscaleConvertor: GrayscaleConvertor = new LinearGrayscaleConvertor,
                        ) extends ImageProcessor {
  override def processImage(): Unit = {
    // Import the image
    val rgbImage: RGBImage = importer.importImage()
    // Convert to grayscale
    val grayscaleImage: GrayscaleImage = grayscaleConvertor.convert(rgbImage)
    // Apply filters
    val filteredImage: GrayscaleImage = filters.foldLeft(grayscaleImage) { (currentImage, filter) =>
      filter.apply(currentImage)
    }
    // Convert to ASCII
    val asciiImage: AsciiImage = asciiConvertor.convert(filteredImage)
    // Export the image
    exporter.exportImage(asciiImage)
  }
}
