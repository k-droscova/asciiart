package Services.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes}
import Services.ImageConvertors.AsciiConvertor.{BourkeLinearAsciiConvertor, BorderedAsciiConvertor, DefaultLinearAsciiConvertor, CustomLinearAsciiConvertor}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized
import org.mockito.Mockito._
import org.mockito.MockedConstruction
class AsciiTableCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {
  private var parser: AsciiTableCommandLineParserImpl = uninitialized
  private var defaultTableMock: MockedConstruction[DefaultLinearAsciiConvertor] = uninitialized
  private var customTableMock: MockedConstruction[CustomLinearAsciiConvertor] = uninitialized
  private var borderedTableMock: MockedConstruction[BorderedAsciiConvertor] = uninitialized
  private var bourkeTableMock: MockedConstruction[BourkeLinearAsciiConvertor] = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new AsciiTableCommandLineParserImpl()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("No input returns default table") {
    defaultTableMock = mockConstruction(classOf[DefaultLinearAsciiConvertor], (_, _) => {})
    val converter = parser.parse("random input for testing")
    assert(converter.isInstanceOf[DefaultLinearAsciiConvertor])
    defaultTableMock.close()
  }

  test("Valid input with default table") {
    defaultTableMock = mockConstruction(classOf[DefaultLinearAsciiConvertor], (_, _) => {})
    val converter = parser.parse("--table=default")
    assert(converter.isInstanceOf[DefaultLinearAsciiConvertor])
    defaultTableMock.close()
  }

  test("Valid input with bourke table") {
    bourkeTableMock = mockConstruction(classOf[BourkeLinearAsciiConvertor], (_, _) => {})
    val converter = parser.parse("--table=bourke")
    assert(converter.isInstanceOf[BourkeLinearAsciiConvertor])
    bourkeTableMock.close()
  }

  test("Valid input with custom table") {
    customTableMock = mockConstruction(classOf[CustomLinearAsciiConvertor], (mocked, context) => {
      assert("custom chars" == context.arguments.get(0).asInstanceOf[String])
    })
    val converter = parser.parse("--custom-table \"custom chars\"")
    assert(converter.isInstanceOf[CustomLinearAsciiConvertor])
    customTableMock.close()
  }
}
