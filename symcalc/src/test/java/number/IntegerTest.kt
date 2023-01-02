package org.cerion.symcalc.number

import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.`should equal`
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IntegerTest {

    private val i0 = Integer(0)
    private val i1 = Integer(1)
    private val i3 = Integer(3)
    private val i5 = Integer(5)
    private val neg = Integer(-17)
    private val big = Integer("99999999999999999999999999999999999999999999")

    @Test
    fun stringConstructor() {
        verify(Integer("0"), 0)
        verify(Integer("5"), 5)
        verify(Integer("-5"), -5)
        verify(Integer("1234567890"), 1234567890)
        verify(Integer("-1234567890"), -1234567890)

        verify(Integer("9999999999999999999999999999999999999999"), BigInteger("9999999999999999999999999999999999999999"))
    }

    @Test
    fun constants() {
        verify(Integer.ZERO, 0)
        verify(Integer.ONE, 1)
        verify(Integer.TWO, 2)
    }

    @Test
    fun negate() {
        verify(-zero, 0)
        verify(-one, -1)
        verify(-two, -2)
        verify(-Integer(-5), 5)
    }

    @Test
    fun addition() {
        //Identity
        verify(i0 + i1, 1)
        verify(i1 + i0, 1)

        //Basic
        verify(i1 + i1, 2)
        verify(i0 + i0, 0)
        verify(i3 + i5, 8)

        //Negative
        verify(neg + i5, -12)
        verify(i5 + neg, -12)
        verify(neg + neg, -34)

        //Big
        verify(big + big, BigInteger("199999999999999999999999999999999999999999998"))
        verify(big + i1, BigInteger("100000000000000000000000000000000000000000000"))
        verify(big + big.unaryMinus(), 0)
    }

    @Test
    fun subtraction() {
        //Zero
        verify(i0 - i0, 0)
        verify(i0 - i1, -1)
        verify(i1 - i0, 1)

        //Basic
        verify(i1 - i1, 0)
        verify(i3 - i5, -2)
        verify(i5 - i3, 2)

        //Negative
        verify(neg - i5, -22)
        verify(i5 - neg, 22)
        verify(neg - neg, 0)

        //Big
        verify(big - big, BigInteger("0"))
        verify(big - i1, BigInteger("99999999999999999999999999999999999999999998"))
        verify(i1 - big, BigInteger("-99999999999999999999999999999999999999999998"))
        verify(big - big.unaryMinus(), BigInteger("199999999999999999999999999999999999999999998"))
    }

    @Test
    fun multiply() {
        //Zero
        verify(i0 * i0, 0)
        verify(i0 * i1, 0)
        verify(i1 * i0, 0)

        //Basic
        verify(i1 * i1, 1)
        verify(i3 * i5, 15)

        //Negative
        verify(neg * i5, -85)
        verify(i5 * neg, -85)
        verify(neg * neg, 289)

        //Big
        verify(big * big, BigInteger("9999999999999999999999999999999999999999999800000000000000000000000000000000000000000001"))
        verify(big * i1, BigInteger("99999999999999999999999999999999999999999999"))
        verify(i1 * big, BigInteger("99999999999999999999999999999999999999999999"))
        verify(big * big.unaryMinus(), BigInteger("-9999999999999999999999999999999999999999999800000000000000000000000000000000000000000001"))
    }

    @Test
    fun divide() {
        assertEquals(Integer(-1), Integer(1) / Integer(-1))
        assertEquals(Integer(-1), Integer(-1) / Integer(1))
        assertEquals(Integer(1), Integer(-1) / Integer(-1))

        assertEquals(Integer(5), Integer(10) / two)
        assertEquals(Integer(20), Integer(10) / Rational(1,2))
        assertEquals(Rational(21, 4), Integer(3) / Rational(4,7))
        assertEquals(RealDouble(4.273504273504273), Integer(10) / RealDouble(2.34))
        assertEquals(RealBigDec("4.27"), Integer(10) / RealBigDec("2.34"))
        assertEquals(Integer(2), Integer(10) / Complex(5,0))
        assertEquals(Complex(Rational(8,13),Rational(-12,13)), Integer(4) / Complex(2,3))
    }

    @Test
    fun divideByZero() {
        assertFailsWith<ArithmeticException> { one / zero}
        assertFailsWith<ArithmeticException> { one / Rational(zero, one)}
        assertFailsWith<ArithmeticException> { one / RealDouble(0.0) }
        assertFailsWith<ArithmeticException> { one / Complex.ZERO}
    }

    @Test
    fun inc_dec() {
        var n = Integer.NEGATIVE_ONE
        assertEquals(Integer.ZERO, ++n)
        assertEquals(Integer.ONE, ++n)
        assertEquals(Integer.TWO, ++n)

        assertEquals(Integer.ONE, --n)
        assertEquals(Integer.ZERO, --n)
        assertEquals(Integer.NEGATIVE_ONE, --n)
    }

    @Test
    fun compareTo() {
        assertEquals(0, Integer(5).compareTo(Integer(5)))
        assertEquals(-1, Integer(5).compareTo(Integer(6)))
        assertEquals(1, Integer(6).compareTo(Integer(5)))

        assertEquals(0, zero.compareTo(Rational(0,1)))
        assertEquals(-1, Integer(5).compareTo(Rational(11,2)))
        assertEquals(1, Integer(5).compareTo(Rational(9,2)))

        assertEquals(0, Integer(5).compareTo(RealDouble(5.0)))
        assertEquals(-1, Integer(5).compareTo(RealDouble(5.00000001)))
        assertEquals(1, Integer(5).compareTo(RealDouble(4.999999999)))

        assertEquals(0, Integer.TWO.compareTo(Complex(2, 0)))
    }

    @Test
    fun compareTo_LargeNumbers() {
        val a = Integer("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999998")
        val b = Integer("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999")
        assertEquals(1, b.compareTo(a))
        assertEquals(1, one.compareTo(Rational(one,a)))
        assertEquals(-1, zero.compareTo(Rational(one,a)))
        assertEquals(1, a.compareTo(Rational(b, Integer(7))))
    }

    @Test
    fun compareTo_complex() {
        assertFailsWith<UnsupportedOperationException> { one.compareTo(Complex(0, 1)) }
    }

    @Test
    fun pow() {
        Integer.ONE pow Integer(-2) `should equal` 1
        Integer(-3) pow Integer(5) `should equal` -243
        Integer(12) pow Integer(50) `should equal` Integer("910043815000214977332758527534256632492715260325658624")
        Integer(3) pow Integer(-5) `should equal` Rational(1, 243)
    }

    @Test
    fun powToRational() {
        assertEquals(Integer.ONE, Integer(1).pow(Rational(7, 5)))
        assertEquals(Integer(4), Integer(16).pow(Rational(1,2)))
        assertEquals(Integer(3125), Integer(125).pow(Rational(5,3)))
        assertEquals(Rational(Integer.ONE, Integer(3125)), Integer(125).pow(Rational(-5,3)))
        assertEquals(Integer(7), Integer(16807).pow(Rational(1,5)))
    }

    @Test
    fun powtoRational_negativeRoot() {
        assertEquals(Complex(0, 2), Integer(-4) pow Rational(1,2))
        assertEquals(Integer(-2), Integer(-8) pow Rational(1,3))
    }

    @Test
    fun powToRational_improperFraction() {
        Integer(2) pow Rational(11, 2) `should equal` Times(32, Power(2, Rational.HALF))
        Integer(5) pow Rational(23, 4) `should equal` Times(3125, Power(5, Rational(3, 4)))
    }

    @Test
    fun powToRational_largeSqrt() {
        // Special case sqrt is much faster if perfect square
        assertEquals(Integer("314159265358979"), Integer("98696044010893382709735922441").pow(Rational.HALF))
    }

    @Test
    fun powToRational_partialeval() {
        // Not able to fully evaluate
        assertEquals(Power(Integer(3), Rational(1,3)), Integer(3).pow(Rational(1,3)))
        assertEquals(Power(Integer(29), Rational(2,3)), Integer(29).pow(Rational(2,3)))
        assertEquals(Power(Integer(23), Rational(1,2)), Integer(529).pow(Rational(1,4)))
        assertEquals(Power(Integer(6), Rational.HALF), Integer(6).pow(Rational.HALF))
    }

    @Test
    fun powToRational_partialReduce() {
        assertEquals(Times(3, Power(3, Rational(1,3))), Integer(81).pow(Rational.THIRD))
        assertEquals(Times(3, Power(3, Rational(2,3))), Integer(243).pow(Rational(1,3)))
        assertEquals(Times(Power(5, Rational(2,3)), Power(7, Rational(1,3))), Integer(175).pow(Rational.THIRD))
    }

    @Test
    fun pow_toReal() {
        Integer(3) pow RealDouble(2.5) `should equal` 15.588457268119896
        Integer(223) pow RealDouble(-1.2) `should equal` 0.001520667150293348
        Integer("100000000000000000000000000000") pow RealDouble(0.13) `should equal` 5888.436553555892
        Integer(123) pow RealDouble(123.0) `should equal` 1.1437436793461719E257
        Integer(3) pow RealDouble(3.14) `should equal` 31.489135652454948

        // Big Decimal
        Integer(3) pow RealBigDec("3.1415926535897932385") `should equal` "31.544280700197543962"
    }

    @Test
    fun intValue() {
        assertEquals(Int.MAX_VALUE, Integer("2147483647").intValue())
        assertEquals(Int.MIN_VALUE, Integer("-2147483648").intValue())
        assertFailsWith<OperationException> { Integer("2147483648").intValue() }
        assertFailsWith<OperationException> { Integer("-2147483649").intValue() }
    }

    @Test
    fun toPrecision() {
        assertEquals(Integer(5), Integer(5).toPrecision(Expr.InfinitePrecision))
        assertEquals(RealDouble(5.0), Integer(5).toPrecision(Expr.MachinePrecision))

        assertEquals("5.0000`5", Integer(5).toPrecision(5).toString())
        assertEquals("25.000`5", Integer(25).toPrecision(5).toString())
        assertEquals("12300`3", Integer(12321).toPrecision(3).toString())
        assertEquals("123460000`5", Integer(123456789).toPrecision(5).toString())
    }

    @Test
    fun quotient() {
        Integer(6).quotient(Integer(5)) `should equal` 1
        Integer(4).quotient(Integer(5)) `should equal` 0
        Integer(-25).quotient(Integer(6)) `should equal` -5

        Integer(7).quotient(Rational(2, 7)) `should equal` 24
        Integer(-7).quotient(Rational(2, 7)) `should equal` -25

        Integer(7).quotient(RealDouble(0.33)) `should equal` 21
        Integer(-7).quotient(RealDouble(0.33)) `should equal` -22

        Integer(7).quotient(RealBigDec("0.33")) `should equal` 21
        Integer(-7).quotient(RealBigDec("0.33")) `should equal` -22

        Integer(70).quotient(Complex(2,4)) `should equal` Complex(7, -14)
        Integer(-70).quotient(Complex(5,2)) `should equal` Complex(-12, 5)
    }

    @Test
    fun mod() {
        Integer(7) % Integer(3) `should equal` 1
        Integer(-7) % Integer(3)  `should equal` 2

        Integer(7) % Rational(2, 3) `should equal` Rational(1, 3)
        Integer(-7) % Rational(5, 3) `should equal` Rational(4, 3)

        Integer(7) % RealDouble(1.33) `should equal` 0.34999999999999964
        Integer(-7) % RealDouble(1.33) `should equal` 0.9800000000000004

        Integer(7) % RealBigDec("1.33") `should equal` "0.350"
        Integer(-7) % RealBigDec("1.33") `should equal` "0.980"

        Integer(7) % Complex(2, 0.37) `should equal` Complex(0.6299999999999999, 0.8900000000000001)
    }

    private fun verify(e: NumberExpr, expected: Long) {
        assertEquals(NumberType.INTEGER, e.numType, "unexpected type: " + e.numType)

        val n = e as Integer
        assertEquals(expected, n.intValue().toLong())
    }

    private fun verify(e: NumberExpr, expected: BigInteger) {
        assertEquals(NumberType.INTEGER, e.numType, "unexpected type: " + e.numType)

        val n = e as Integer
        assertEquals(expected, n.value)
    }

    companion object {
        val zero = Integer.ZERO
        val one = Integer.ONE
        val two = Integer.TWO
    }
}
