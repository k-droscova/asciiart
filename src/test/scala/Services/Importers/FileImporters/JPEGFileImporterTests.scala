package Services.Importers.FileImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.compiletime.uninitialized

class JPEGFileImporterTests extends AnyFunSuite with BeforeAndAfterEach {
  private var validJpgFile: File = uninitialized
  private var nonExistentFile: File = uninitialized
  private var unsupportedFile: File = uninitialized
  private val tempDir = System.getProperty("java.io.tmpdir")

  override def beforeEach(): Unit = {
    super.beforeEach()

    validJpgFile = new File(tempDir, "JPEGFileImporterTests_validImage.jpg")
    val jpgImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(jpgImage, "jpg", validJpgFile)

    nonExistentFile = new File(tempDir, "JPEGFileImporterTests_nonExistentFile.jpg")

    unsupportedFile = new File(tempDir, "JPEGFileImporterTests_unsupportedImage.png")
    val unsupportedImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(unsupportedImage, "png", unsupportedFile)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    validJpgFile.delete()
    unsupportedFile.delete()
  }

  test("JPEGFileImporter should throw BaseError if the file doesn't exist") {
    val thrown = intercept[BaseError] {
      new JPEGFileImporter(nonExistentFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileNotFound)
  }

  test("JPEGFileImporter should throw BaseError if the path is not a file") {
    val thrown = intercept[BaseError] {
      new JPEGFileImporter(tempDir)
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
