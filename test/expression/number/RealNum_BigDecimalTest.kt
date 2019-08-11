package expression.number

import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

class RealNum_BigDecimalTest {

    @Test
    fun precision() {
        assertEquals(11, RealNum_BigDecimal("1.0000000001").precision)
        assertEquals(1, RealNum_BigDecimal("0.0000000001").precision)
        assertEquals(2, RealNum_BigDecimal("0.0000000011").precision)
        assertEquals(13, RealNum_BigDecimal("111.0000000011").precision)
        assertEquals(23, RealNum_BigDecimal("1.1234567899999987654321").precision)
    }

    @Test
    fun precision_Zero() {
        assertEquals(RealNum_BigDecimal("0.0000000001"), RealNum_BigDecimal("0.0000000001") + RealNum_BigDecimal("0.0000000000"))
    }

    @Test
    fun precision_evaluate() {
        assertEquals(RealNum_BigDecimal("111.000000001"), RealNum_BigDecimal("111.0000000011").evaluate(12))
    }

    @Test
    fun precision_lowestOfTwo() {
        val a = RealNum_BigDecimal("0.33")
        val b = RealNum_BigDecimal("0.3333333333")

        // PrecisionA + PrecisionB = Precision of lowest
        assertEquals(RealNum_BigDecimal("0.66"), a + b)
        assertEquals(RealNum_BigDecimal("0.00"), a - b)
        assertEquals(RealNum_BigDecimal("0.11"), a * b)
        assertEquals(RealNum_BigDecimal("0.99"), a / b)

        // using N()
        assertEquals(RealNum_BigDecimal("5.85987"), Plus(N(E(), IntegerNum(10)), N(Pi(), IntegerNum(5))).eval())
    }

    @Test
    fun multiply() {
        assertEquals(RealNum_BigDecimal("0.0002468"), RealNum_BigDecimal("0.0001234") * IntegerNum.TWO)
        assertEquals(RealNum_BigDecimal("0.0001851"), RealNum_BigDecimal("0.0001234") * Rational(3,2))
        assertEquals(RealNum.create(0.0037427219999999995), RealNum_BigDecimal("0.0001234") * RealNum.create(30.33))
        assertEquals(RealNum_BigDecimal("0.003743"), RealNum_BigDecimal("0.0001234") * RealNum_BigDecimal("30.33"))
        assertEquals(Complex(RealNum_BigDecimal("0.0002468"),RealNum_BigDecimal("0.0003702")), RealNum_BigDecimal("0.0001234") * Complex(2,3))
    }

    @Test
    fun divide() {
        assertEquals(RealNum_BigDecimal("0.0000617"), RealNum_BigDecimal("0.0001234") / IntegerNum.TWO)
        assertEquals(RealNum_BigDecimal("0.00008227"), RealNum_BigDecimal("0.0001234") / Rational(3,2))
        assertEquals(RealNum_Double(0.03297472469502143), RealNum_BigDecimal("1.0001234") / RealNum.create(30.33))
        assertEquals(RealNum_BigDecimal("0.03297"), RealNum_BigDecimal("1.0001234") / RealNum_BigDecimal("30.33"))
        assertEquals(Complex(RealNum_BigDecimal("0.00001898"),RealNum_BigDecimal("-0.00002848")), RealNum_BigDecimal("0.0001234") / Complex(2,3))
    }

    @Test
    fun pow() {
        assertEquals(RealNum_BigDecimal("36"), power("3.14", "3.14"))
        assertEquals(RealNum_BigDecimal("36.45"), power("3.1415", "3.1415"))
        assertEquals(RealNum_BigDecimal("36.462159607207911770990826022"), power("3.14159265358979323846264338328", "3.14159265358979323846264338328"))
        assertEquals(RealNum_BigDecimal("36.46"), power("3.1415", "3.14159265358979323846264338328"))
        assertEquals(RealNum_BigDecimal("36.46"), power("3.14159265358979323846264338328", "3.1415"))

        assertEquals(RealNum_BigDecimal("13.2696645139"), power("3.00000000000","2.35340583128859694839201928385968473749596868726265"))
    }

    @Test
    fun pow_negativeExp() {
        assertEquals(RealNum_BigDecimal("0.028"), power("3.14", "-3.14"))
        assertEquals(RealNum_BigDecimal("0.02743"), power("3.1415", "-3.1415"))
    }

    @Test
    fun pow_negativeBase() {
        // Expected for now...
        assertFailsWith<ArithmeticException> { power("-3.14", "3.14") }
    }

    private fun power(a: String, b: String): NumberExpr {
        return RealNum_BigDecimal(a).pow(RealNum_BigDecimal(b))
    }
}