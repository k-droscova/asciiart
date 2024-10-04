package Services.CommandLineParsing

import Services.ImageLoader.{FileImageLoader, ImageLoader, RandomImageLoader}
import Services.Logging.Errors.{BaseError, GeneralErrorCodes, ImageLoadingErrorCodes}
import Services.Logging.{LogContext, LogSeverity, LogSource}

object LoaderFactory extends CommandLineParser[ImageLoader] {

  override def parse(args: Map[String, String]): ImageLoader = {
    validateConflictingArgs(args, List(("--image", "--image-random")))

    if (args.contains("--image-random")) {
      val width = args.get("--width").map(validateInt(_, "--width"))
      val height = args.get("--height").map(validateInt(_, "--height"))
      val minWidth = validateInt(args.getOrElse("--minWidth", "50"), "--minWidth")
      val maxWidth = validateInt(args.getOrElse("--maxWidth", "150"), "--maxWidth")
      val minHeight = validateInt(args.getOrElse("--minHeight", "50"), "--minHeight")
      val maxHeight = validateInt(args.getOrElse("--maxHeight", "150"), "--maxHeight")

      new RandomImageLoader(width, height, minWidth, maxWidth, minHeight, maxHeight)
    } else if (args.contains("--image")) {
      val imagePath = args("--image")
      if (imagePath.trim.isEmpty) {
        throw BaseError(
          message = "Image path cannot be empty.",
          context = LogContext.UI,
          errorCode = ImageLoadingErrorCodes.FileNotFound
        )
      }
      new FileImageLoader(imagePath)
    } else {
      throw BaseError(
        message = "Either --image <path> or --image-random must be specified.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      )
    }
  }
}