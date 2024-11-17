package UI.CommandLineParsers.ImporterParser

import Core.Errors.{BaseError, GeneralErrorCodes}
import Services.Importers.RandomImporters.RandomImporter
import UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers.RandomImporterCommandLineParser
import org.scalatest.funsuite.AnyFunSuite

class RandomImporterCommandLineParserTests extends AnyFunSuite {

  private val parser = new RandomImporterCommandLineParser

  test("RandomImporterCommandLineParser returns Some(RandomImporter) when --image-random is present") {
    val args = Array("--image-random")
    val result = parser.parse(args)

    assert(result.isRight)
    result match {
      case Right(Some(importer: RandomImporter)) => succeed
      case _ => fail("Expected Right(Some(RandomImporter)) but got something else.")
    }
  }

  test("RandomImporterCommandLineParser returns None when --image-random is absent") {
    val args = Array("--image", "path/to/image.jpg")
    val result = parser.parse(args)

    assert(result.isRight)
    result match {
      case Right(None) => succeed
      case _ => fail("Expected Right(None) but got something else.")
    }
  }

  test("RandomImporterCommandLineParser handles an empty argument array gracefully") {
    val args = Array.empty[String]
    val result = parser.parse(args)

    assert(result.isRight)
    result match {
      case Right(None) => succeed
      case _ => fail("Expected Right(None) but got something else.")
    }
  }

  test("RandomImporterCommandLineParser ignores unrelated arguments") {
    val args = Array("--unrelated-arg", "--another-arg")
    val result = parser.parse(args)

    assert(result.isRight)
    result match {
      case Right(None) => succeed
      case _ => fail("Expected Right(None) but got something else.")
    }
  }

  test("RandomImporterCommandLineParser throws an error when multiple --image-random arguments are present") {
    val args = Array("--image-random", "--image-random")
    val result = parser.parse(args)

    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.message.contains("The --image-random argument should only occur once."))
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
      case _ => fail("Expected Left(BaseError) but got something else.")
    }
  }
}
