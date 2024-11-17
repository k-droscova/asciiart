package UI.CommandLineParsers.FilterParser.SpecializedFilterParsers

import Core.Errors.BaseError
import Services.Filters.InvertFilter

/**
 * Parses the `--invert` argument to create a list of `InvertFilter`.
 *
 * If the `--invert` argument is present, it adds one `InvertFilter` to the list.
 * Multiple occurrences of `--invert` result in multiple `InvertFilter` instances.
 */
class InvertFilterCommandLineParser extends SpecializedFilterCommandLineParser[InvertFilter] {
  override def parse(args: Array[String]): Either[BaseError, Option[List[InvertFilter]]] = {
    val invertCount = args.count(_ == "--invert")

    if (invertCount > 0) {
      Right(Some(List.fill(invertCount)(new InvertFilter())))
    } else {
      Right(None)
    }
  }
}