package UI.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Nonlinear.BorderedAsciiTable
import UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers.BorderedAsciiTableCommandLineParser
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized

class BorderedAsciiTableCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {

  private var borderedTableMock: MockedConstruction[BorderedAsciiTable] = uninitialized
  private var parser: BorderedAsciiTableCommandLineParser = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new BorderedAsciiTableCommandLineParser()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("Should return None if --table=bordered argument is not present") {
    val args = Array("--some-other-arg", "value")
    val result = parser.parse(args)
    assert(result == Right(None))
  }

  test("Should return BorderedAsciiTable if --table=bordered argument is valid") {
    borderedTableMock = mockConstruction(classOf[BorderedAsciiTable], (mocked, context) => {
      assert("custom-chars" == context.arguments.get(0).asInstanceOf[String])
      assert(context.arguments.get(1).asInstanceOf[List[Int]] == List(1, 2, 3))
    })

    val args = Array("--table=bordered", "custom-chars", "[1,2,3]")
    val result = parser.parse(args)

    result match {
      case Right(Some(table)) =>
        assert(table == borderedTableMock.constructed().get(0))
      case _ => fail("Expected Right(Some(BorderedAsciiTable)), but got something else.")
    }

    borderedTableMock.close()
  }

  test("Should return error if --table=bordered is specified multiple times") {
    val args = Array("--table=bordered", "chars1", "[1,2]", "--table=bordered", "chars2", "[3,4]")
    val result = parser.parse(args)

    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Only one --table=bordered argument is allowed."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }

  test("Should return error if custom characters are missing after --table=bordered") {
    val args = Array("--table=bordered")
    val result = parser.parse(args)

    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Custom characters must be specified after --table=bordered argument."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }

  test("Should return error if another command follows after --table=bordered") {
    val args = Array("--table=bordered", "--hello")
    val result = parser.parse(args)

    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Custom characters must be specified after --table=bordered argument."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }

  test("Should return error if borders are missing after characters") {
    val args = Array("--table=bordered", "custom-chars")
    val result = parser.parse(args)

    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Borders must be specified after the characters in the format [int,int,...]."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }
}
