package Services.Logging.Errors
sealed trait ErrorCodes {
  def code: Int
}

// Image loading error codes (100-199)
sealed trait ImageLoadingErrorCodes extends ErrorCodes
object ImageLoadingErrorCodes {
  case object UnsupportedFormat extends ImageLoadingErrorCodes { val code: Int = 100 }
  case object FileNotFound extends ImageLoadingErrorCodes { val code: Int = 101 }
  case object FileUnreadable extends ImageLoadingErrorCodes { val code: Int = 102 }
  case object InvalidImageData extends ImageLoadingErrorCodes { val code: Int = 103 }
}

// ASCII conversion error codes (200-299)
sealed trait ASCIIConversionErrorCodes extends ErrorCodes
object ASCIIConversionErrorCodes {
  case object ConversionFailed extends ASCIIConversionErrorCodes { val code: Int = 200 }
  case object InvalidTable extends ASCIIConversionErrorCodes { val code: Int = 201 }
  case object TableNotFound extends ASCIIConversionErrorCodes { val code: Int = 202 }
}

// Filter application error codes (300-399)
sealed trait FilterErrorCodes extends ErrorCodes
object FilterErrorCodes {
  case object InvalidRotationAngle extends FilterErrorCodes { val code: Int = 300 }
  case object InvalidScaleFactor extends FilterErrorCodes { val code: Int = 301 }
  case object FilterApplicationFailed extends FilterErrorCodes { val code: Int = 302 }
}

// Output error codes (400-499)
sealed trait OutputErrorCodes extends ErrorCodes
object OutputErrorCodes {
  case object FileSaveFailed extends OutputErrorCodes { val code: Int = 400 }
  case object ConsoleOutputFailed extends OutputErrorCodes { val code: Int = 401 }
  case object UnsupportedOutputFormat extends OutputErrorCodes { val code: Int = 402 }
}

// General application error codes (500-599)
sealed trait GeneralErrorCodes extends ErrorCodes
object GeneralErrorCodes {
  case object InvalidArgument extends GeneralErrorCodes { val code: Int = 500 }
  case object UnknownError extends GeneralErrorCodes { val code: Int = 501 }
}