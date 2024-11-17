package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Importers.RandomImporters.RandomImporter

/**
 * Parses command line arguments to determine if random image generation is requested.
 *
 * The `RandomImporterCommandLineParser` is a specialized parser for the `--image-random` argument,
 * which indicates that the user wants to generate a random image instead of loading one from a file.
 *
 * Argument Format:
 * - `--image-random`: Generates a random image with both width and height in the range [1, 800].
 *
 * Validation:
 * - The `--image-random` argument should only occur once.
 *
 * Example:
 * ```
 * --image-random
 * ```
 *
 * Errors:
 * - Returns a `BaseError` if:
 *   - Multiple `--image-random` arguments are detected.
 */
class RandomImporterCommandLineParser extends SpecializedImporterCommandLineParser[RandomImporter] {
  override def parse(args: Array[String]): Either[BaseError, Option[RandomImporter]] = {
    val randomArgsCount = args.count(_ == "--image-random")

    if (randomArgsCount > 1) {
      Left(BaseError(
        message = "The --image-random argument should only occur once.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    } else if (randomArgsCount == 1) {
      Right(Some(new RandomImporter()))
    } else {
      Right(None)
    }
  }
}