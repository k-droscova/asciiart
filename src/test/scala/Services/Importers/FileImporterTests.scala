package Services.Importers

import Core.Errors.{BaseError, ImageLoadingErrorCodes}
import Core.Models.Image.RGBImage
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import scala.compiletime.uninitialized
class FileImporterTests extends AnyFunSuite with BeforeAndAfterEach {
  private var validJpgFile: File = uninitialized
  private var validPngFile: File = uninitialized
  private var validGifFile: File = uninitialized
  private var onePixelFile: File = uninitialized
  private var nonExistentFile: File = uninitialized
  private var notAFile: File = uninitialized
  private val tempDir = System.getProperty("java.io.tmpdir")

  override def beforeEach(): Unit = {
    super.beforeEach()

    validJpgFile = new File(tempDir, "testImage.jpg")
    val jpgImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(jpgImage, "jpg", validJpgFile)

    validPngFile = new File(tempDir, "testImage.png")
    val pngImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(pngImage, "png", validPngFile)

    validGifFile = new File(tempDir, "testImage.gif")
    val gifImage = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(gifImage, "gif", validGifFile)

    onePixelFile = new File(tempDir, "onePixelImage.png")
    val onePixelImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    ImageIO.write(onePixelImage, "png", onePixelFile)

    nonExistentFile = new File(tempDir, "nonExistentFile.jpg")

    notAFile = new File(tempDir, "notAFileDir")
    notAFile.mkdir()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    validJpgFile.delete()
    validPngFile.delete()
    validGifFile.delete()
    onePixelFile.delete()
    if (notAFile.exists()) {
      notAFile.delete()
    }
  }

  test("FileImporter should throw BaseError if the file doesn't exist") {
    val thrown = intercept[BaseError] {
      new FileImporter(nonExistentFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileNotFound)
  }

  test("FileImporter should throw BaseError if the path is not a file") {
    val thrown = intercept[BaseError] {
      new FileImporter(notAFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.FileUnreadable)
  }

  test("FileImporter should throw BaseError if the image format is unsupported") {
    val unsupportedFile = new File(tempDir, "unsupportedFile.bmp")
    unsupportedFile.createNewFile()

    val thrown = intercept[BaseError] {
      new FileImporter(unsupportedFile.getPath)
    }
    assert(thrown.errorCode == ImageLoadingErrorCodes.UnsupportedFormat)
  }

  test("FileImporter should load a valid JPG image") {
    val importer = new FileImporter(validJpgFile.getPath)
    val image = importer.importImage()
    assert(image.isInstanceOf[RGBImage])
    assert(image.getWidth == 200)
    assert(image.getHeight == 100)
  }

  test("FileImporter should load a valid PNG image") {
    val importer = new FileImporter(validPngFile.getPath)
    val image = importer.importImage()
    assert(image.isInstanceOf[RGBImage])
    assert(image.getWidth == 200)
    assert(image.getHeight == 100)
  }

  test("FileImporter should load a valid GIF image") {
    val importer = new FileImporter(validGifFile.getPath)
    val image = importer.importImage()
    assert(image.isInstanceOf[RGBImage])
    assert(image.getWidth == 200)
    assert(image.getHeight == 100)
  }

  test("FileImporter should handle a 1x1 image gracefully") {
    val importer = new FileImporter(onePixelFile.getPath)
    val image = importer.importImage()
    assert(image.isInstanceOf[RGBImage])
    assert(image.getWidth == 1)
    assert(image.getHeight == 1)
    val pixel = image.getPixel(0, 0)
    assert(pixel.red == 0)
    assert(pixel.green == 0)
    assert(pixel.blue == 0)
  }
}
