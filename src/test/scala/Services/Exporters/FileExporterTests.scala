package Services.Exporters

import Core.Errors.{BaseError, OutputErrorCodes}
import Core.Models.Image.AsciiImage
import Core.Models.Pixel.AsciiPixel
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Path}
import scala.compiletime.uninitialized
import scala.util.Using

class FileExporterTests extends AnyFunSuite with BeforeAndAfterEach {
  private var exporter: FileExporter = uninitialized
  private var image: AsciiImage = uninitialized
  private var tempDir: Path = uninitialized
  private val fileName = "testOutput.txt"
  override def beforeEach(): Unit = {
    super.beforeEach()
    tempDir = Files.createTempDirectory("testDir")
    image = new AsciiImage(Vector(
      Vector(AsciiPixel('A'), AsciiPixel('B'), AsciiPixel('C'), AsciiPixel('D'), AsciiPixel('E')),
      Vector(AsciiPixel('F'), AsciiPixel('G'), AsciiPixel('H'), AsciiPixel('I'), AsciiPixel('J')),
      Vector(AsciiPixel('K'), AsciiPixel('L'), AsciiPixel('M'), AsciiPixel('N'), AsciiPixel('O'))
    ))
  }

  override def afterEach(): Unit = {
    super.afterEach()
    exporter = null
    image = null
    if (Files.exists(tempDir)) {
      Files.list(tempDir).forEach(path => Files.delete(path))
      Files.delete(tempDir)
    }
  }

  test("FileExporter should throw BaseError if file already exists") {
    val existingFile = tempDir.resolve(fileName).toFile
    Using.resource(new PrintWriter(existingFile)) { writer =>
      writer.println("Dummy content")
    }
    val thrown = intercept[BaseError] {
      exporter = new FileExporter(existingFile.getPath)
    }
    assert(thrown.errorCode == OutputErrorCodes.FileSaveFailed)
  }

  test("FileExporter should create a new file and save the image content") {
    val newFile = tempDir.resolve(fileName).toFile
    assert(!newFile.exists(), "File should not exist before exporting.")

    exporter = new FileExporter(newFile.getPath)
    exporter.exportImage(image)

    assert(newFile.exists(), "File should exist after exporting.")

    val expectedContent = "ABCDE\nFGHIJ\nKLMNO\n"
    val content = Files.readString(newFile.toPath)
    assert(content == expectedContent, "The file content should match the exported ASCII image.")
  }

  test("FileExporter should create subdirectories if they do not exist") {
    val subdirectoryPath = tempDir.resolve("subdir1/subdir2").resolve(fileName).toString
    val subdirectory = new File(tempDir.resolve("subdir1/subdir2").toString)
    assert(!subdirectory.exists(), "Subdirectories should not exist before exporting.")

    exporter = new FileExporter(subdirectoryPath)
    exporter.exportImage(image)

    assert(subdirectory.exists(), "Subdirectories should exist after exporting.")
    assert(subdirectory.isDirectory, "The path should be a directory.")
    Files.list(subdirectory.toPath).forEach(path => Files.delete(path))
    Files.delete(subdirectory.toPath)
  }

  test("FileExporter should add .txt extension if not specified") {
    val filePathWithoutExtension = tempDir.resolve("outputFile").toString
    exporter = new FileExporter(filePathWithoutExtension)
    exporter.exportImage(image)

    val savedFile = new File(filePathWithoutExtension + ".txt")
    assert(savedFile.exists(), "File should exist after exporting with .txt extension.")
    assert(savedFile.isFile, "The saved path should be a file.")
  }

  test("FileExporter should throw BaseError if PrintWriter throws an error") {
    mockConstruction(classOf[PrintWriter], (mockedWriter, context) => {
      doThrow(new RuntimeException("Print error")).when(mockedWriter).println(any[String])
    })

    val filePath = tempDir.resolve(fileName).toString
    exporter = new FileExporter(filePath)

    val thrown = intercept[BaseError] {
      exporter.exportImage(image)
    }
    assert(thrown.errorCode == OutputErrorCodes.FileSaveFailed)
    assert(thrown.message.contains("Print error"))
    assert(!new File(filePath).exists(), "File should be deleted after write failure.")
  }
}
