package org.cerion.symcalc.expression.number

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.function.core.N
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals


class RationalTest : NumberTestBase() {

    @Test
    fun equals() {
        assertEquals(Rational(4, 5), Rational(4, 5))
        assertNotEquals(Rational(4, 5), Rational(4, 4))
        assertNotEquals(Rational(4, 5), Rational(5, 4))
    }

    @Test
    fun compareTo() {
        assertEquals(-1, Rational(1,3).compareTo(Integer(1)))
        assertEquals(1, Rational(1,2).compareTo(Integer(0)))
        assertEquals(0, Rational(16,4).compareTo(Integer(4)))

        assertEquals(-1, Rational(1,3).compareTo(Rational(1,2)))
        assertEquals(1, Rational(10,3).compareTo(Rational(7,3)))
        assertEquals(0, Rational(1,2).compareTo(Rational(2,4)))

        assertEquals(-1, Rational(-1,2).compareTo(RealDouble(-0.2)))
        assertEquals(1, Rational(1,2).compareTo(RealDouble(0.4)))
        assertEquals(0, Rational(1,2).compareTo(RealDouble(0.5)))

        assertEquals(-1, Rational(1,3).compareTo(Complex(1,0)))
        assertEquals(1, Rational(4,3).compareTo(Complex(1,0)))
    }

    @Test
    fun compareTo_complex() {
        assertFailsWith<UnsupportedOperationException> { Rational(1,2).compareTo(Complex(0, 1)) }
    }

    @Test
    fun negate() {
        assertEquals(Rational(4, 5), Rational(-4, 5).unaryMinus())
        assertEquals(Rational(-4, 5), Rational(4, 5).unaryMinus())
        assertEquals(Rational(-4, 5), Rational(-4, -5).unaryMinus())
        assertEquals(Rational(4, 5), Rational(4, -5).unaryMinus())
    }

    @Test
    fun eval_normalizesNegative() {
        var n = Rational(2,-3)
        assertEquals(Integer(2), n.numerator)
        assertEquals(Integer(-3), n.denominator)

        n = n.eval() as Rational
        assertEquals(Integer(-2), n.numerator)
        assertEquals(Integer(3), n.denominator)

        n = Rational(-2,-3).eval() as Rational
        assertEquals(Integer(2), n.numerator)
        assertEquals(Integer(3), n.denominator)
    }

    @Test
    fun eval_reduces() {
        assertEquals(Integer.TWO, Rational(2, 4).numerator)
        assertEquals(Integer.ONE, (Rational(2, 4).eval() as Rational).numerator)

        // Except with Hold
        assertEquals(Hold(Rational(2, 4)), Hold(Rational(2, 4)).eval())
    }

    @Test
    fun eval_precision() {
        assertEquals(RealBigDec("0"), Rational(0,1).eval(10))
    }

    @Test
    fun eval_toReal() {
        assertEquals(RealDouble(0.3333333333333333), N(Rational(1,3)).eval())

        val bigDec = N(Rational(1,3), Integer(50)).eval() as RealBigDec
        assertEquals(50, bigDec.precision)
        assertEquals(RealBigDec("0.33333333333333333333333333333333333333333333333333"), bigDec)
    }

    @Test
    fun eval_storedPrecision() {
        val real = Rational(1,3).toPrecision(5) as RealBigDec
        assertEquals(20, real.value.precision())
    }

    @Test
    fun addition() {
        //Integer
        Rational(1, 2) + Rational(1, 2) `should equal` 1
        assertEquals(Integer(1), Rational(1, 2) + Rational(2, 4))
        assertEquals(Integer(0), Rational(-1, 2) + Rational(2, 4))
        assertEquals(Integer.TWO, Rational(1, 1) + Integer.ONE)

        //Rational
        assertEquals(Rational(3, 2), Rational(1, 1) + Rational(1, 2))
        assertEquals(Rational(1, 2), Rational(-1, 2) + Integer.ONE)
    }

    @Test
    fun subtract() {
        //Integer
        assertEquals(Integer.ZERO, Rational(1, 2) - Rational(1, 2))
        assertEquals(Integer.ZERO, Rational(1, 2) - Rational(2, 4))
        assertEquals(Integer.TWO, Rational(5, 2) - Rational(1, 2))
        assertEquals(Integer(-5), Rational(1, 2) - Rational(11, 2))

        //Rational
        assertEquals(Rational(1, 2), Rational(1, 1) - Rational(1, 2))
        assertEquals(Rational(-1, 2), Rational(1, 2) - Integer.ONE)
    }

    @Test
    fun divide() {
        // TODO_LP add others

        // Real
        assertEquals(RealDouble(3.9808917197452227), Divide(Rational(25, 2), RealDouble(3.14)).eval())
    }

    @Test
    fun pow_toInteger() {
        assertEquals(Rational(1,8), Rational.HALF pow Integer(3))
        assertEquals(Rational(1048576, 9765625), Rational(4,5) pow Integer(10))
        assertEquals(Rational(9765625, 1048576), Rational(4,5) pow Integer(-10))
        assertEquals(Rational(Integer.ONE, Integer("4294967296")), Rational(1,2) pow Integer(32))
        assertEquals(Rational(Integer.ONE, Integer("340282366920938463463374607431768211456")), Rational(1,2) pow Integer(128))
    }

    @Test
    fun pow_toReal() {
        assertEquals(RealDouble(0.1088188204120155), Rational.HALF pow RealDouble(3.2))
        assertEquals(RealDouble(9.18958683997628), Rational.HALF pow RealDouble(-3.2))

        assertEquals(RealBigDec("0.11332"), Rational.HALF pow RealBigDec("3.1415"))
        assertEquals(RealBigDec("0.1133147323"), Rational.HALF pow RealBigDec("3.141592654"))
        assertEquals(RealBigDec("8.82497783"), Rational.HALF pow RealBigDec("-3.141592654"))
    }

    @Test
    fun floor() {
        Assertions.assertEquals(Integer(0), Rational(1, 2).floor())
        Assertions.assertEquals(Integer(1), Rational(3, 2).floor())
        Assertions.assertEquals(Integer(-1), Rational(-1, 2).floor())
    }

    @Test
    fun quotient() {
        Rational(27, 2).quotient(Integer(3)) `should equal` 4
        Rational(-27, 2).quotient(Integer(3)) `should equal` -5

        Rational(27, 2).quotient(Rational(2, 3)) `should equal` 20
        Rational(-27, 2).quotient(Rational(2, 3)) `should equal` -21

        Rational(27, 2).quotient(RealDouble(0.33)) `should equal` 40
        Rational(-27, 2).quotient(RealDouble(0.33)) `should equal` -41

        Rational(27, 2).quotient(RealBigDec("0.33")) `should equal` 40
        Rational(-27, 2).quotient(RealBigDec("0.33")) `should equal` -41

        Rational(21, 2).quotient(Complex(3,2)) `should equal` Complex(2, -2)
        Rational(-100, 2).quotient(Complex(2,3)) `should equal` Complex(-8, 12)
    }

    @Test
    fun mod() {
        Rational(50, 3) % Integer(7) `should equal` Rational(8,3)
        Rational(-50, 3) % Integer(7)  `should equal` Rational(13,3)
        Rational(50, 3) % Integer(-7) `should equal` Rational(-13,3)
        Rational(-50, 3) % Integer(-7)  `should equal` Rational(-8,3)

        Rational(50, 3) % Rational(1,2) `should equal` Rational(1,6)
        Rational(-50, 3) % Rational(1,2)  `should equal` Rational(1,3)

        Rational(50, 3) % RealDouble(1.33) `should equal` 0.706666666666667
        Rational(-50, 3) % RealDouble(1.33) `should equal` 0.6233333333333313

        Rational(50, 3) % RealBigDec("1.34") `should equal` "0.587"
        Rational(-50, 3) % RealBigDec("1.33") `should equal` "0.623"

        Rational(50, 3) % Complex(2, 4) `should equal` Complex(Rational(2,3), -2)
    }
}