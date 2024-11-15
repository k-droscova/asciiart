package Services.Importers.FileImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes}
import Core.Models.Image.RGBImage
import Services.Importers.FileImporters.JPEGFileImporter
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.compiletime.uninitialized

class JPEGFileImporterTests extends AnyFunSuite with BeforeAndAfterEach {
  private var validJpgFile: File = uninitialized
  private var nonExistentFile: File = uninitialized
  private var notAFile: File = uninitialized
  private var unsupportedFile: File = uninitialized
  private val tempDir = System.getProperty("java.io.tmpdir")

  override def beforeEach(): Unit = {
    super.beforeEach()

    validJpgFile = new File(tempDir, "testImage.jpg")
    val jpgImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(jpgImage, "jpg", validJpgFile)

    nonExistentFile = new File(tempDir, "nonExistentFile.jpg")

    notAFile = new File(tempDir, "notAFileDir")
    notAFile.mkdir()

    unsupportedFile = new File(tempDir, "unsupportedFile.png")
    val unsupportedImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(unsupportedImage, "png", unsupportedFile)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    validJpgFile.delete()
    unsupportedFile.delete()
    if (notAFile.exists()) notAFile.delete()
  }

  test("JPEGFileImporter should throw BaseError if the file doesn't exist") {
    val thrown = intercept[BaseError] {
      new JPEGFileImporter(nonExistentFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileNotFound)
  }

  test("JPEGFileImporter should throw BaseError if the path is not a file") {
    val thrown = intercept[BaseError] {
      new JPEGFileImporter(notAFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileUnreadable)
  }

  test("JPEGFileImporter should throw BaseError if the image format is unsupported") {
    val thrown = intercept[BaseError] {
      new JPEGFileImporter(unsupportedFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.UnsupportedFormat)
  }

  test("JPEGFileImporter should load a valid JPG image") {
    val importer = new JPEGFileImporter(validJpgFile.getPath)
    val image = importer.importImage()
    assert(image.getWidth == 200)
    assert(image.getHeight == 100)
  }
}
