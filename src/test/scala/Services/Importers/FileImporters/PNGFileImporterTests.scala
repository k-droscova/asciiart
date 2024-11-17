package Services.Importers.FileImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.compiletime.uninitialized

class PNGFileImporterTests extends AnyFunSuite with BeforeAndAfterEach {
  private var validPngFile: File = uninitialized
  private var nonExistentFile: File = uninitialized
  private var unsupportedFile: File = uninitialized
  private val tempDir = System.getProperty("java.io.tmpdir")

  override def beforeEach(): Unit = {
    super.beforeEach()

    validPngFile = new File(tempDir, "PNGFileImporterTests_validImage.png")
    val pngImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(pngImage, "png", validPngFile)

    nonExistentFile = new File(tempDir, "PNGFileImporterTests_nonExistentFile.png")

    unsupportedFile = new File(tempDir, "PNGFileImporterTests_unsupportedImage.jpg")
    val unsupportedImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(unsupportedImage, "jpg", unsupportedFile)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    validPngFile.delete()
    unsupportedFile.delete()
  }

  test("PNGFileImporter should throw BaseError if the file doesn't exist") {
    val thrown = intercept[BaseError] {
      new PNGFileImporter(nonExistentFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileNotFound)
  }

  test("PNGFileImporter should throw BaseError if the path is not a file") {
    val thrown = intercept[BaseError] {
      new PNGFileImporter(tempDir)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileUnreadable)
  }

  test("PNGFileImporter should throw BaseError if the image format is unsupported") {
    val thrown = intercept[BaseError] {
      new PNGFileImporter(unsupportedFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.UnsupportedFormat)
  }

  test("PNGFileImporter should load a valid PNG image") {
    val importer = new PNGFileImporter(validPngFile.getPath)
    val image = importer.importImage()
    assert(image.getWidth == 200)
    assert(image.getHeight == 100)
  }
}
