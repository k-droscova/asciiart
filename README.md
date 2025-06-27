# ASCII Art

![Scala CI](https://github.com/k-droscova/asciiart/actions/workflows/ci.yml/badge.svg)

⚠️ This project was created as part of coursework at FIT CTU.
Please **do not copy or submit** it as your own if you're a student at any university.
Feel free to read and learn from it, but **plagiarism is a violation of academic integrity**.

The **ASCII Art** project allows you to transform images into visually stunning ASCII art. Additionally, you can apply various filters to the image, adjust its appearance, and export the results to the console or a file.

---

## Features

- **Image to ASCII Conversion:** Convert supported image formats (e.g., JPEG, PNG) to ASCII art.
- **Customizable ASCII Tables:** Use various predefined or custom ASCII tables for conversion.
- **Filters:** Apply filters like rotation, brightness adjustment, or inversion to enhance the ASCII art.
- **Export Options:** Save the output as a text file or print it directly to the console.
- **Extensibility:** Designed with modularity in mind, making it easy to add new filters or features.

---

## Usage Instructions

### Running the Application
1. **Basic Command:**
   Use the `sbt` command to run the application with the desired parameters:

    ```bash
    sbt "run --image <path_to_image> --output-console --brightness 10"
    ```

2. **Available Command-Line Options:**

- `--image <path>`: Specifies the path to the input image.
- `--image-random`: Generates random image.
- `--output-console`: Outputs the ASCII art to the console.
- `--output-file <path>`: Saves the ASCII art to the specified file.
- `--table=<type>`: Specifies the ASCII table to use (`default`, `custom`, `bourke`, or `bordered`).
- `--brightness <value>`: Adjusts the brightness of the image. The value must be an integer.
- `--rotate <angle>`: Rotates the image by the specified angle (must be a multiple of 90 degrees).
- `--invert`: Applies an invert filter to the image.

# Testing

## Running Tests

The project includes positive and negative test scripts to validate functionality and error handling.

1. **Positive Tests:**

   These tests ensure the application behaves as expected under valid input conditions.

   Run the positive tests using:

   ```bash
    test_positive.sh
   ```

   You can see the output of the saturn.jpeg image (from resources) in the output/positive_tests_saturn directory.
   
2. **Negative Tests:**

   These tests verify that the application correctly handles invalid input and edge cases.

   Run the negative tests using:

   ```bash
    test_negative.sh
   ```
