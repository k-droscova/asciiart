package Business

import Business.ImageProcessor
import Core.Models.Image.{AsciiImage, GrayscaleImage, RGBImage}
import Services.Exporters.Exporter
import Services.Filters.Filter
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import Services.ImageConvertors.GrayscaleConvertor.{GrayscaleConvertor, LinearGrayscaleConvertor}
import Services.Importers.Importer

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
