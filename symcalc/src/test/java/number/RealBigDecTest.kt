package org.cerion.symcalc.number

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.`should equal`
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private infix fun String.pow(other: String): NumberExpr {
    return RealBigDec(this) pow RealBigDec(other)
}

class RealBigDecTest : NumberTestBase() {

    @Test
    fun storedPrecision() {
        assertEquals(19, RealBigDec.getStoredPrecision(0))
        assertEquals(28, RealBigDec.getStoredPrecision(10))
        assertEquals(38, RealBigDec.getStoredPrecision(20))
        assertEquals(38, RealBigDec.getStoredPrecision(21))
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

        // Small numbers are not displayed in scientific notation
        assertEquals("0.000000000000000000000000000001`1", RealBigDec("0.000000000000000000000000000001").toString())
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
    fun internalStorageAddition() {
        val a = RealBigDec("1234.")
        val mag = fun(b: String): Int = ((a + RealBigDec(b)) as RealBigDec).value.precision()

        assertEquals(8, mag("0.1234"))
        assertEquals(10, mag("0.001234"))
        assertEquals(11, mag("0.0001234"))
        assertEquals(19, mag("0.0000000000001234"))
        assertEquals(19, mag("0.00000000000001234"))
        assertEquals(19, mag("0.0000000000000000001234"))
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
        assertEquals(RealBigDec("5.8599"), N(E(), Integer(10)) + N(Pi(), Integer(5)))
    }

    @Test
    fun add() {
        RealBigDec("0.0000000001") + RealBigDec("0.0000000000") `should equal` "0.0000000001"
        RealBigDec("0.0000000") + RealBigDec("0.3607293") `should equal` "0.3607293"

        // Different internal precision
        RealBigDec("0.3333333333", 2) + RealBigDec("0.3333333333", 10) `should equal` "0.67"
        N(Rational(1,3), 2) + N(Rational(1,3), 10) `should equal` "0.67"
    }

    @Test
    fun multiply() {
        assertEquals(RealBigDec("0.0002468"), RealBigDec("0.0001234") * Integer.TWO)
        assertEquals(RealBigDec("0.0001851"), RealBigDec("0.0001234") * Rational(3,2))
        assertEquals(RealDouble(0.0037427219999999995), RealBigDec("0.0001234") * RealDouble(30.33))
        assertEquals(RealBigDec("0.003743"), RealBigDec("0.0001234") * RealBigDec("30.33"))
        assertEquals(Complex("0.0002468", "0.0003702"), RealBigDec("0.0001234") * Complex(2,3))

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
        assertEquals(19, bd3.value.precision())
    }

    @Test
    fun divide() {
        assertEquals(RealBigDec("0.0000617"), RealBigDec("0.0001234") / Integer.TWO)
        assertEquals(RealBigDec("0.00008227"), RealBigDec("0.0001234") / Rational(3,2))
        assertEquals(RealDouble(0.03297472469502143), RealBigDec("1.0001234") / RealDouble(30.33))
        assertEquals(RealBigDec("0.03297"), RealBigDec("1.0001234") / RealBigDec("30.33"))
        assertEquals(Complex("0.00001898", "-0.00002848"), RealBigDec("0.0001234") / Complex(2,3))
    }

    @Test
    fun dividePrecision() {
        val n1 = (RealBigDec("4.00") / RealBigDec("2.00"))
        assertEquals("2.00`3", n1.toString())
        assertEquals(1, n1.value.precision())

        val n2 = (RealBigDec("2.00") / RealBigDec("3.01"))
        assertEquals("0.664`3", n2.toString())
        assertEquals(19, n2.value.precision())

        val n3 = (RealBigDec("40000000000000.0") / RealBigDec("20000000000000.0"))
        assertEquals("2.00000000000000`15", n3.toString())
        assertEquals(1, n3.value.precision())
    }

    @Test
    fun pow_toInteger() {
        assertEquals(RealBigDec("1.0002468"), RealBigDec("1.0001234") pow Integer.TWO)
        assertEquals(RealBigDec("93648.04760"), RealBigDec("3.141592654") pow Integer(10))
        assertEquals(RealBigDec("8769956796.0826994748"), (N(Pi(),Integer(20)).eval() as NumberExpr) pow Integer(20))
        assertEquals(RealBigDec("3.40282366920938463463374607431768211E+38"), RealBigDec("2.00000000000000000000000000000000000") pow Integer(128))
    }

    @Test
    fun pow_toRational() {
        assertEquals(RealBigDec("2"), RealBigDec("4.0") pow Rational.HALF)
        assertEquals(RealBigDec("1.00006170"), RealBigDec("1.0001234") pow Rational(1,2))

        assertEquals(RealBigDec("1.587"), RealBigDec("4.000") pow Rational(1,3))
        assertEquals(RealBigDec("0.001171"), RealBigDec("0.0001234") pow Rational(3,4))
        assertEquals(RealBigDec("854.1"), RealBigDec("0.0001234") pow Rational(-3,4))
    }

    @Test
    fun pow_toRational_large() {
        // Rational is evaluated differently for large int values where power+ Nth root is not practical
        RealBigDec("2.0", 50) pow Rational(Integer("34359738367"), Integer("17179869183")) `should equal` "4.0000000001613859042092866284144245566543954355456"
    }

    @Test
    fun pow_toRational_storedPrecision() {
        val pow = (RealBigDec("2.000001") pow Rational(1,3)) as RealBigDec
        assertEquals(BigDecimal("1.259921259881679816"), pow.value)
    }

    @Test
    fun pow() {
        "3.14" pow "3.14" `should equal` "36.3"
        "3.1415" pow "3.1415" `should equal` "36.455"
        "3.1415" pow "-3.1415" `should equal` "0.027431"
        "3.1415" pow "3.14159265358979323846264338328" `should equal` "36.459"
        "3.14159265358979323846264338328" pow "3.1415" `should equal` "36.458"
        "3.00000000000" pow "2.35340583128859694839201928385968473749596868726265" `should equal` "13.2696645139"
        "3.14159265358979323846264338328" pow "3.14159265358979323846264338328" `should equal` "36.4621596072079117709908260227"

        // Double
        RealBigDec("3.14") pow RealDouble(3.14) `should equal` 36.33783888017471
    }

    @Test
    fun pow_negativeExp() {
        assertEquals(RealBigDec("0.0275"), "3.14" pow "-3.14")
        assertEquals(RealBigDec("0.027431"), "3.1415" pow "-3.1415")
    }

    @Test
    fun pow_negativeRoot() = assertAll(
        Power("-4.0000", Rational.HALF) `==` Complex(0, "2.0000"),
        Power("-8.0000", Rational.THIRD) `==` Complex("1.0000", "1.7321"),
        Power("-1.2345", Rational(3,2)) `==` Complex(0, "-1.3716")
    )

    @Test
    fun powStoredPrecision() {
        val p1 = RealBigDec("1.23").pow(RealBigDec("1.23"))
        assertEquals(RealBigDec("1.29"), p1)
        assertEquals(3, p1.precision)
        assertEquals(19, p1.value.precision())

        // Precision should be minimum of the 2 values
        val p2 = RealBigDec("1.23456789999999999999").pow(RealBigDec("1.23"))
        assertEquals(RealBigDec("1.30"), p2)
        assertEquals(3, p2.precision)
        assertEquals(19, p2.value.precision())
    }

    @Test
    fun exp() {
        assertAll(
                RealBigDec("0.0").exp() `==` "1.0",
                RealBigDec("3.1415").exp() `==` "23.139",
                (Pi().eval(50) as RealBigDec).exp() `==` "23.140692632779269005729086367948547380266106242600",
                RealBigDec("-10.99922222").exp() `==` "0.00001671469609")
    }

    @Test
    fun expLarge() {
        val x = RealBigDec("2.0", 100)
        x.exp() `should equal` "7.389056098930650227230427460575007813180315570551847324087127822522573796079057763384312485079121795"
    }

    @Test
    fun floor() {
        RealBigDec("-21.12").floor() `should equal` Integer("-22")
        RealBigDec("50000000000000000000000000000000.1").floor() `should equal` Integer("50000000000000000000000000000000")
        RealBigDec("50000000000000000000000000000000.9").floor() `should equal` Integer("50000000000000000000000000000000")
    }

    @Test
    fun round() {
        RealBigDec("50000000000000000000000000000000.4").round() `should equal` Integer("50000000000000000000000000000000")
        RealBigDec("50000000000000000000000000000000.6").round() `should equal` Integer("50000000000000000000000000000001")
    }

    @Test
    fun mod() {
        RealBigDec("5.2") % Integer(2) `should equal` "1.2"
        RealBigDec("-5.2") % Integer(2) `should equal` "0.8"

        RealBigDec("5.2") % Rational(1, 2) `should equal` "0.2"
        RealBigDec("-5.2") % Rational(1, 2) `should equal` "0.3"

        RealBigDec("5.2") % RealDouble(0.5) `should equal` 0.20000000000000018
        RealBigDec("-5.2") % RealDouble(0.5) `should equal` 0.2999999999999998

        RealBigDec("5.2") % RealBigDec("0.5") `should equal` "0.2"
        RealBigDec("-5.2") % RealBigDec("0.5") `should equal` "0.3"

        RealBigDec("5.2") % Complex(2,4) `should equal` Complex("-0.8", -2)
        RealBigDec("-50.2") % Complex(5,4) `should equal` Complex("-0.2", -1)
    }

    @Test
    fun quotient() {
        RealBigDec("5.2").quotient(Integer(2)) `should equal` 2
        RealBigDec("-5.2").quotient(Integer(2)) `should equal` -3

        RealBigDec("5.2").quotient(Rational(1, 2)) `should equal` 10
        RealBigDec("-5.2").quotient(Rational(1, 2)) `should equal` -11

        RealBigDec("5.2").quotient(RealDouble(0.5)) `should equal` 10
        RealBigDec("-5.2").quotient(RealDouble(0.5)) `should equal` -11

        RealBigDec("5.2").quotient(RealBigDec("0.5")) `should equal` 10
        RealBigDec("-5.2").quotient(RealBigDec("0.5")) `should equal` -11

        RealBigDec("5.2").quotient(Complex(2,4)) `should equal` Complex(1, -1)
        RealBigDec("-50.2").quotient(Complex(5,4)) `should equal` Complex(-6, 5)
    }

    @Test
    fun sqrt() {
        val x = Pi().eval(100) as RealBigDec
        x.sqrt() `should equal` "1.772453850905516027298167483341145182797549456122387128213807789852911284591032181374950656738544665"
    }

    @Test
    fun nthRoot() {
        val x = RealBigDec("11.0", 100)
        x.root(3) `should equal` "2.223980090569315521165363376722157196518699128096923055699345808660400983082975974489758054481626274"
        x.root(7) `should equal` "1.408543888428699411406584628756831160498853412347820244689226302716888429032334406113507802751386845"
    }
}