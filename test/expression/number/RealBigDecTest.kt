package expression.number

import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class RealBigDecTest : NumberTestBase() {

    @Test
    fun storedPrecision() {
        assertEquals(20, RealBigDec.getStoredPrecision(0))
        assertEquals(20, RealBigDec.getStoredPrecision(10))
        assertEquals(30, RealBigDec.getStoredPrecision(11))
        assertEquals(30, RealBigDec.getStoredPrecision(20))
        assertEquals(40, RealBigDec.getStoredPrecision(21))
    }

    @Test
    fun toStringPrecision() {
        assertEquals("3.14`3", RealBigDec("3.14").toString())
        assertEquals("3.1415`5", RealBigDec("3.1415").toString())

        // If stored precision is higher it should round down to set precision
        assertEquals("3.142`4", RealBigDec(BigDecimal("3.1415"), 4).toString())

        // If stored precision is higher it should round down to set precision
        assertEquals("2.00`3", RealBigDec(BigDecimal("2"), 3).toString())
        assertEquals("12345.0`6", RealBigDec(BigDecimal("12345"), 6).toString())
        assertEquals("0.0220000`6", RealBigDec(BigDecimal("0.022"), 6).toString())
    }

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
    fun accuracy() {
        assertEquals(0, RealBigDec("100").accuracy)
        assertEquals(1, RealBigDec("3.0").accuracy)
        assertEquals(2, RealBigDec("3.14").accuracy)
        assertEquals(9, RealBigDec("0.000000001").accuracy)
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
        var x = RealBigDec("111.0000000011").toPrecision(12) as RealBigDec
        assertEquals(RealBigDec("111.000000001"), x)
        assertEquals(12, x.precision)
        assertEquals(9, x.accuracy)

        x = RealBigDec("0.141567").toPrecision(5) as RealBigDec
        assertEquals(RealBigDec("0.14157"), x)
        assertEquals(5, x.precision)
        assertEquals(5, x.accuracy)
    }

    @Test
    fun precision_lowestOfTwo() {
        val a = RealBigDec("0.33")
        val b = RealBigDec("0.3333333333")

        // PrecisionA + PrecisionB = Precision of lowest
        assertEquals(RealBigDec("0.66"), a + b)
        assertEquals(RealBigDec("-0.0033"), a - b)
        assertEquals(RealBigDec("0.11"), a * b)
        assertEquals(RealBigDec("0.99"), a / b)

        // using N()
        assertEquals(RealBigDec("5.8599"), Plus(N(E(), Integer(10)), N(Pi(), Integer(5))).eval())
    }

    @Test
    fun add() {
        assertEquals(RealBigDec("0.0000000001"), RealBigDec("0.0000000001") + RealBigDec("0.0000000000"))
        assertEquals(RealBigDec("0.3607293"), RealBigDec("0.0000000") + RealBigDec("0.3607293"))

        // Different internal precision
        assertEquals(RealBigDec("0.67"), RealBigDec("0.3333333333", 2) + RealBigDec("0.3333333333", 10))
        assertEquals(RealBigDec("0.67"), Plus(N(Rational(1,3),Integer(2)), N(Rational(1,3),Integer(10))).eval())
    }

    @Test
    fun multiply() {
        assertEquals(RealBigDec("0.0002468"), RealBigDec("0.0001234") * Integer.TWO)
        assertEquals(RealBigDec("0.0001851"), RealBigDec("0.0001234") * Rational(3,2))
        assertEquals(RealDouble(0.0037427219999999995), RealBigDec("0.0001234") * RealDouble(30.33))
        assertEquals(RealBigDec("0.003743"), RealBigDec("0.0001234") * RealBigDec("30.33"))
        assertEquals(Complex(RealBigDec("0.0002468"),RealBigDec("0.0003702")), RealBigDec("0.0001234") * Complex(2,3))

        // Different internal precision
        assertEquals(RealBigDec("0.89"), RealBigDec("0.67") * RealBigDec("1.333333333"))
    }

    @Test
    fun multiplyStoredPrecision() {
        val bd1 = RealBigDec("0.33") * RealBigDec("0.33")
        assertEquals(4, bd1.value.precision())

        val bd2 = RealBigDec("0.333333333") * RealBigDec("0.333333333")
        assertEquals(18, bd2.value.precision())

        val bd3 = RealBigDec("0.333333333333333333333333333333", 9) * RealBigDec("0.3333")
        assertEquals(20, bd3.value.precision())
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
    fun dividePrecision() {
        assertEquals("2.00`3", (RealBigDec("4.00") / RealBigDec("2.00")).toString())
    }

    @Test
    fun pow() {
        assertEquals(RealBigDec("36.3"), power("3.14", "3.14"))
        assertEquals(RealBigDec("36.455"), power("3.1415", "3.1415"))
        assertEquals(RealBigDec("36.4621596072079117709908260227"), power("3.14159265358979323846264338328", "3.14159265358979323846264338328"))
        assertEquals(RealBigDec("36.459"), power("3.1415", "3.14159265358979323846264338328"))
        assertEquals(RealBigDec("36.458"), power("3.14159265358979323846264338328", "3.1415"))

        assertEquals(RealBigDec("13.2696645139"), power("3.00000000000","2.35340583128859694839201928385968473749596868726265"))
    }

    @Test
    fun powStoredPrecision() {
        val p1 = RealBigDec("1.23").pow(RealBigDec("1.23"))
        assertEquals(20, p1.value.precision())
    }

    @Test
    fun exp() {
        assertAll(
                { assertEquals(RealBigDec("1.0"), RealBigDec("0.0").exp()) },
                { assertEquals(RealBigDec("23.139"), RealBigDec("3.1415").exp()) },
                { assertEquals(RealBigDec("23.140692632779269005729086367948547380266106242600"), (Pi().eval(50) as RealBigDec).exp()) },
                { assertEquals(RealBigDec("0.00001671469609"), RealBigDec("-10.99922222").exp()) }
        )
    }

    @Test
    fun pow_negativeExp() {
        assertEquals(RealBigDec("0.0275"), power("3.14", "-3.14"))
        assertEquals(RealBigDec("0.027431"), power("3.1415", "-3.1415"))
    }

    @Test
    fun pow_negativeBase() {
        // Expected for now...
        assertFailsWith<OperationException> { power("-3.14", "3.14") }
    }

    private fun power(a: String, b: String): NumberExpr {
        return RealBigDec(a).pow(RealBigDec(b))
    }
}