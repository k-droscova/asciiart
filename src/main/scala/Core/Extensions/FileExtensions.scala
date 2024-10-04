package Core.Extensions
import java.io.File

/**
 * Extension method for File to validate existence, type, and readability.
 */
object FileExtensions {

  /**
   * Extension for the File class that validates the file.
   * Ensures the file exists, is a regular file, and is readable.
   * Throws IllegalArgumentException if any check fails.
   */
  extension (file: File) {
    def validate(): Unit = {
      if (!file.exists()) {
        throw new IllegalArgumentException(s"File not found: ${file.getPath}")
      }
      if (!file.isFile) {
        throw new IllegalArgumentException(s"Not a valid file: ${file.getPath}")
      }
      if (!file.canRead) {
        throw new IllegalArgumentException(s"Cannot read file: ${file.getPath}")
      }
    }
  }
}