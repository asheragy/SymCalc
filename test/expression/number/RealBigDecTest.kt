package expression.number

import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class RealBigDecTest : NumberTestBase() {

    @Test
    fun identity() {
        val n = RealBigDec("3.141592653589793238462643383279")

        assertAdd(n, n, Integer.ZERO)
        assertAdd(n, n, Rational.ZERO)
        assertAdd(RealDouble(3.141592653589793), n, RealDouble(0.0))
        assertAdd(n, n, RealBigDec(BigDecimal(0.0)))
        assertAdd(n, n, Complex.ZERO)
    }

    @Test
    fun precision() {
        assertEquals(10, RealBigDec("1234567890").precision)
        assertEquals(10, RealBigDec("12.34567890").precision)
        assertEquals(10, RealBigDec("12345678.90").precision)
        assertEquals(10, RealBigDec(".1234567890").precision)
        assertEquals(10, RealBigDec(".0000000000000000000001234567890").precision)
        assertEquals(11, RealBigDec("12345678900").precision)

        assertEquals(11, RealBigDec("1.0000000001").precision)
        assertEquals(1, RealBigDec("0.0000000001").precision)
        assertEquals(2, RealBigDec("0.0000000011").precision)
        assertEquals(13, RealBigDec("111.0000000011").precision)
        assertEquals(23, RealBigDec("1.1234567899999987654321").precision)
    }

    @Test
    fun isZero() {
        assertTrue(RealBigDec("0.0").isZero)
        assertTrue(RealBigDec("0").isZero)
        assertTrue(RealBigDec("0.00000000").isZero)
        assertTrue((RealBigDec("3.14") - RealBigDec("3.14")).isZero)
    }

    @Test
    fun precision_evaluate() {
        assertEquals(RealBigDec("111.000000001"), RealBigDec("111.0000000011").evaluate(12))
    }

    @Test
    fun precision_lowestOfTwo() {
        val a = RealBigDec("0.33")
        val b = RealBigDec("0.3333333333")

        // PrecisionA + PrecisionB = Precision of lowest
        assertEquals(RealBigDec("0.66"), a + b)
        assertEquals(RealBigDec("0.00"), a - b)
        assertEquals(RealBigDec("0.11"), a * b)
        assertEquals(RealBigDec("0.99"), a / b)

        // using N()
        assertEquals(RealBigDec("5.8599"), Plus(N(E(), Integer(10)), N(Pi(), Integer(5))).eval())
    }

    @Test
    fun add() {
        assertEquals(RealBigDec("0.0000000001"), RealBigDec("0.0000000001") + RealBigDec("0.0000000000"))
        assertEquals(RealBigDec("0.3607293"), RealBigDec("0.0000000") + RealBigDec("0.3607293"))
    }

    @Test
    fun multiply() {
        assertEquals(RealBigDec("0.0002468"), RealBigDec("0.0001234") * Integer.TWO)
        assertEquals(RealBigDec("0.0001851"), RealBigDec("0.0001234") * Rational(3,2))
        assertEquals(RealDouble(0.0037427219999999995), RealBigDec("0.0001234") * RealDouble(30.33))
        assertEquals(RealBigDec("0.003743"), RealBigDec("0.0001234") * RealBigDec("30.33"))
        assertEquals(Complex(RealBigDec("0.0002468"),RealBigDec("0.0003702")), RealBigDec("0.0001234") * Complex(2,3))
    }

    @Test
    fun divide() {
        assertEquals(RealBigDec("0.0000617"), RealBigDec("0.0001234") / Integer.TWO)
        assertEquals(RealBigDec("0.00008227"), RealBigDec("0.0001234") / Rational(3,2))
        assertEquals(RealDouble(0.03297472469502143), RealBigDec("1.0001234") / RealDouble(30.33))
        assertEquals(RealBigDec("0.03297"), RealBigDec("1.0001234") / RealBigDec("30.33"))
        assertEquals(Complex(RealBigDec("0.00001898"),RealBigDec("-0.00002848")), RealBigDec("0.0001234") / Complex(2,3))
    }

    @Test
    fun pow() {
        assertEquals(RealBigDec("36"), power("3.14", "3.14"))
        assertEquals(RealBigDec("36.45"), power("3.1415", "3.1415"))
        assertEquals(RealBigDec("36.462159607207911770990826022"), power("3.14159265358979323846264338328", "3.14159265358979323846264338328"))
        assertEquals(RealBigDec("36.46"), power("3.1415", "3.14159265358979323846264338328"))
        assertEquals(RealBigDec("36.46"), power("3.14159265358979323846264338328", "3.1415"))

        assertEquals(RealBigDec("13.2696645139"), power("3.00000000000","2.35340583128859694839201928385968473749596868726265"))
    }

    @Test
    fun pow_negativeExp() {
        assertEquals(RealBigDec("0.028"), power("3.14", "-3.14"))
        assertEquals(RealBigDec("0.02743"), power("3.1415", "-3.1415"))
    }

    @Test
    fun pow_negativeBase() {
        // Expected for now...
        assertFailsWith<ArithmeticException> { power("-3.14", "3.14") }
    }

    private fun power(a: String, b: String): NumberExpr {
        return RealBigDec(a).pow(RealBigDec(b))
    }
}