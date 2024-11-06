package Core.Models.AsciiTable.Linear

/**
 * BourkeLinearAsciiTable provides an ASCII table for converting grayscale
 * values to ASCII characters based on the Bourke-style mapping. It maps grayscale
 * values (0-255) to characters in the string `"$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. "`
 * in a linear manner, from darkest (`'$'`) to brightest (`' '`).
 *
 * This table provides a richer set of ASCII characters for creating more detailed
 * and visually appealing ASCII art representations of images.
 */
class BourkeLinearAsciiTable extends LinearAsciiTable("$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ") {}
