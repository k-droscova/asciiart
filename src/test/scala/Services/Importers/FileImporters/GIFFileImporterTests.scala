package Services.Importers.FileImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes}
import Core.Models.Image.RGBImage
import Services.Importers.FileImporters.GIFFileImporter
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.compiletime.uninitialized

class GIFFileImporterTests extends AnyFunSuite with BeforeAndAfterEach {
  private var validGifFile: File = uninitialized
  private var nonExistentFile: File = uninitialized
  private var unsupportedFile: File = uninitialized
  private val tempDir = System.getProperty("java.io.tmpdir")

  override def beforeEach(): Unit = {
    super.beforeEach()

    validGifFile = new File(tempDir, "GIFFileImporterTests_validImage.gif")
    val gifImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(gifImage, "gif", validGifFile)

    nonExistentFile = new File(tempDir, "GIFFileImporterTests_nonExistentFile.gif")

    unsupportedFile = new File(tempDir, "GIFFileImporterTests_unsupportedImage.jpg")
    val unsupportedImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(unsupportedImage, "jpg", unsupportedFile)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    validGifFile.delete()
    unsupportedFile.delete()
  }

  test("GIFFileImporter should throw BaseError if the file doesn't exist") {
    val thrown = intercept[BaseError] {
      new GIFFileImporter(nonExistentFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileNotFound)
  }

  test("GIFFileImporter should throw BaseError if the path is not a file") {
    val thrown = intercept[BaseError] {
      new GIFFileImporter(tempDir)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileUnreadable)
  }

  test("GIFFileImporter should throw BaseError if the image format is unsupported") {
    val thrown = intercept[BaseError] {
      new GIFFileImporter(unsupportedFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.UnsupportedFormat)
  }

  test("GIFFileImporter should load a valid GIF image") {
    val importer = new GIFFileImporter(validGifFile.getPath)
    val image = importer.importImage()
    assert(image.getWidth == 200)
    assert(image.getHeight == 100)
  }
}