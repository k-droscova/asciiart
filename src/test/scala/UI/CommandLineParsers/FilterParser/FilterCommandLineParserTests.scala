package UI.CommandLineParsers.FilterParser

import Core.Errors.{BaseError, FilterErrorCodes, GeneralErrorCodes}
import Services.Filters.{BrightnessFilter, InvertFilter, RotateFilter}
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class FilterCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {
  private var parser: FilterCommandLineParserImpl = uninitialized
  private var brightnessMock: MockedConstruction[BrightnessFilter] = uninitialized
  private var invertMock: MockedConstruction[InvertFilter] = uninitialized
  private var rotateMock: MockedConstruction[RotateFilter] = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new FilterCommandLineParserImpl()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("Valid input with brightness filter - number only") {
    brightnessMock = mockConstruction(classOf[BrightnessFilter], (mocked, context) => {
      assert(5 == context.arguments.get(0).asInstanceOf[Int])
    })
    val filters = parser.parse(Array("--brightness", "5"))
    assert(filters.length == 1)
    assert(filters.head.isInstanceOf[BrightnessFilter])
    brightnessMock.close()
  }

  test("Valid input with brightness filter - with + sign") {
    brightnessMock = mockConstruction(classOf[BrightnessFilter], (mocked, context) => {
      assert(5 == context.arguments.get(0).asInstanceOf[Int])
    })
    val filters = parser.parse(Array("--brightness", "+5"))
    assert(filters.length == 1)
    assert(filters.head.isInstanceOf[BrightnessFilter])
    brightnessMock.close()
  }

  test("Valid input with brightness filter - with - sign") {
    brightnessMock = mockConstruction(classOf[BrightnessFilter], (mocked, context) => {
      assert(-5 == context.arguments.get(0).asInstanceOf[Int])
    })
    val filters = parser.parse(Array("--brightness", "-5"))
    assert(filters.length == 1)
    assert(filters.head.isInstanceOf[BrightnessFilter])
    brightnessMock.close()
  }

  test("Valid input with invert filter") {
    invertMock = mockConstruction(classOf[InvertFilter], (mocked, context) => {})
    val filters = parser.parse(Array("--invert"))
    assert(filters.length == 1)
    assert(filters.head.isInstanceOf[InvertFilter])
    invertMock.close()
  }

  test("Valid input with rotate filter - num only") {
    rotateMock = mockConstruction(classOf[RotateFilter], (mocked, context) => {
      assert(90 == context.arguments.get(0).asInstanceOf[Int])
    })
    val filters = parser.parse(Array("--rotate", "90"))
    assert(filters.length == 1)
    assert(filters.head.isInstanceOf[RotateFilter])
    rotateMock.close()
  }

  test("Valid input with rotate filter - with + sign") {
    rotateMock = mockConstruction(classOf[RotateFilter], (mocked, context) => {
      assert(90 == context.arguments.get(0).asInstanceOf[Int])
    })
    val filters = parser.parse(Array("--rotate", "+90"))
    assert(filters.length == 1)
    assert(filters.head.isInstanceOf[RotateFilter])
    rotateMock.close()
  }

  test("Valid input with rotate filter - with - sign") {
    rotateMock = mockConstruction(classOf[RotateFilter], (mocked, context) => {
      assert(-90 == context.arguments.get(0).asInstanceOf[Int])
    })
    val filters = parser.parse(Array("--rotate", "-90"))
    assert(filters.length == 1)
    assert(filters.head.isInstanceOf[RotateFilter])
    rotateMock.close()
  }

  test("Invalid brightness value without argument") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--brightness"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Brightness value must be specified after --brightness."))
  }

  test("Invalid brightness value with non-integer") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--brightness", "123.45"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Invalid brightness argument, expected pattern: (+-)Num where Num is integer"))
  }

  test("Invalid rotation value without argument") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--rotate"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Rotation value must be specified after --rotate."))
  }

  test("Invalid rotation value with non-integer") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--rotate", "-90.123"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Invalid rotation argument, expected pattern: (+-)Num where Num is integer dividable by 90"))
  }

  test("Mixed input with both brightness and invalid argument") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--brightness", "+5", "--rotate", "invalid"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Invalid rotation argument, expected pattern: (+-)Num where Num is integer dividable by 90"))
  }

  test("Valid input with multiple filters") {
    brightnessMock = mockConstruction(classOf[BrightnessFilter], (mocked, context) => {
      assert(5 == context.arguments.get(0).asInstanceOf[Int])
    })
    invertMock = mockConstruction(classOf[InvertFilter], (mocked, context) => {})
    rotateMock = mockConstruction(classOf[RotateFilter], (mocked, context) => {
      assert(90 == context.arguments.get(0).asInstanceOf[Int])
    })

    val filters = parser.parse(Array("--brightness", "5", "--invert", "--rotate", "+90"))
    assert(filters.length == 3)
    assert(filters.exists(_.isInstanceOf[BrightnessFilter]))
    assert(filters.exists(_.isInstanceOf[InvertFilter]))
    assert(filters.exists(_.isInstanceOf[RotateFilter]))

    brightnessMock.close()
    invertMock.close()
    rotateMock.close()
  }

  test("Valid input with all filters in different orders") {
    brightnessMock = mockConstruction(classOf[BrightnessFilter], (mocked, context) => {
      assert(-10 == context.arguments.get(0).asInstanceOf[Int])
    })
    rotateMock = mockConstruction(classOf[RotateFilter], (mocked, context) => {
      assert(180 == context.arguments.get(0).asInstanceOf[Int])
    })
    invertMock = mockConstruction(classOf[InvertFilter], (mocked, context) => {})

    val filters = parser.parse(Array("--rotate", "180", "--invert", "--brightness", "-10"))
    assert(filters.length == 3)
    assert(filters.head.isInstanceOf[RotateFilter])
    assert(filters(1).isInstanceOf[InvertFilter])
    assert(filters(2).isInstanceOf[BrightnessFilter])

    brightnessMock.close()
    rotateMock.close()
    invertMock.close()
  }

  test("Valid input with multiple repeated filters") {
    val filters = parser.parse(Array("--brightness", "5", "--invert", "--rotate", "+90", "--brightness", "10"))

    assert(filters.length == 4)
    assert(filters.count(_.isInstanceOf[BrightnessFilter]) == 2)
    assert(filters.count(_.isInstanceOf[InvertFilter]) == 1)
    assert(filters.count(_.isInstanceOf[RotateFilter]) == 1)
  }

  test("Propagates error from rotate filter constructor when the integer argument is invalid") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--rotate", "+45"))
    }
    assert(thrown.errorCode == FilterErrorCodes.InvalidRotationAngle)
    assert(thrown.message.contains("Angle 45 is invalid. Angle must be a multiple of 90 degrees."))
  }
}
