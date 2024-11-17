package UI

import Core.Errors.BaseError

/**
 * Entry point for the ASCIIArt application.
 *
 * The ConsoleView object initializes the application and delegates processing to the ConsoleViewModel.
 * It captures and reports any errors encountered during execution.
 *
 * Instructions for Running the Application:
 *
 * Allowed Commands:
 * 1. **Input Image (Required):**
 *    - `--image <path>`: Specifies the path to the input image file. The image **must be of type jpeg, png or gif.**
 *    - `--image-random` : Generates random image with both width and height between <1,800> pixels
 *
 * 2. **ASCII Table (Optional):**
 *    - `--table=default`: Use the default ASCII table for conversion.
 *    - `--table=custom "<characters>"`: Use a custom ASCII table defined by `<characters>`.
 *    - `--table=bourke`: Use the Bourke ASCII table for conversion.
 *    - `--table=bordered "<characters>" "[<Int>,<Int>,...]"`: Use a bordered ASCII table with custom borders.
 *       The number of characters must be equal to number of borders + 1
 *            **Important:**
 *            - The number of characters in `<characters>` **must be exactly one more** than the number of borders specified in `[<Int>,<Int>,...]`.
 *            - Example: For borders `[10,20,30]`, `<characters>` must contain **4 characters** (e.g., `".:-="`).
 *
 * 3. **Filters (Optional):**
 *    - `--brightness <Int>`: Adjusts the image brightness by `<Int>` (e.g., `--brightness 50`).
 *    - `--invert`: Inverts the colors of the image.
 *    - `--rotate <Int>`: Rotates the image by `<Int>` degrees. The angle **must** be a multiple of 90 (e.g., `90`, `-90`, `180`, `270`).
 *    - Multiple filters can be applied together.
 *
 * 4. **Output (Required):**
 *    - `--output-file <path>`: Specifies the path to save the output ASCII image.
 *    - `--output-console`: Outputs the ASCII image to the console.
 *
 * Required Arguments:
 * - At least one `--image` argument must be provided.
 * - At least one output method (`--output-file` or `--output-console`) must be specified.
 *
 * Error Handling:
 * - If an error occurs at any stage (e.g., invalid arguments, missing files, or processing errors), the user will be notified with a descriptive error message.
 * - Common issues include:
 *   - Missing required arguments.
 *   - Invalid file paths or unsupported file formats.
 *   - Mismatched table configurations (e.g., incorrect number of characters for bordered tables).
 *   - Invalid filter arguments (e.g., non-multiple of 90 for rotation).
 *
 * Example Usage:
 * - Process an image with the default table and save to a file:
 *   `sbt run -- --image path/to/image.jpg --output-file path/to/output.txt`
 *
 * - Use a custom table and adjust brightness:
 *   `sbt run -- --image path/to/image.jpg --table=custom ".:-=+*#%@" --brightness 50 --output-file path/to/output.txt`
 */
object ConsoleView {
  def main(args: Array[String]): Unit = {
    val viewModel = new ConsoleViewModel()

    try {
      viewModel.run(args)
    } catch {
      case e: BaseError =>
        System.err.println(s"Error: ${e.message}")
      case e: Throwable =>
        System.err.println(s"UNKNOWN ERROR: ${e.getMessage}")
    }
  }
}
