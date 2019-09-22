package org.cerion.symcalc.expression.function.arithmetic

import expression.constant.I
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals

class TimesTest {

    @Test
    fun toString_Parenthesis() {
        assertEquals("2 * 3 * 4", Times(Integer.TWO, Integer(3), Integer(4)).toString())
        assertEquals("2 * (3 + 4)", Times(Integer.TWO, Plus(Integer(3), Integer(4))).toString())
    }

    @Test
    fun timesIntegerZero() {
        assertAll(
                { assertEquals(Integer.ZERO, Times(Integer(5), Integer.ZERO).eval()) },
                { assertEquals(Integer.ZERO, Times(Rational(1,3), Integer.ZERO).eval()) },
                { assertEquals(Integer.ZERO, Times(RealDouble(3.14), Integer.ZERO).eval()) },
                { assertEquals(Integer.ZERO, Times(RealBigDec("3.14"), Integer.ZERO).eval()) },
                { assertEquals(Integer.ZERO, Times(Complex(RealDouble(2.0), Rational.HALF), Integer.ZERO).eval()) },
                { assertEquals(Integer.ZERO, Times(Pi(), Integer.ZERO).eval()) }
        )
    }

    @Test
    fun timesRealZero() {
        assertAll(
                { assertEquals(RealDouble(), Times(Integer(5), RealDouble()).eval()) },
                { assertEquals(RealDouble(), Times(Rational(1,3), RealDouble()).eval()) },
                { assertEquals(RealDouble(), Times(RealDouble(3.14), RealDouble()).eval()) },
                { assertEquals(RealDouble(), Times(RealBigDec("3.14"), RealDouble()).eval()) },
                { assertEquals(RealDouble(), Times(Complex(RealDouble(2.0), Rational.HALF), RealDouble()).eval()) },
                { assertEquals(RealDouble(), Times(Pi(), RealDouble()).eval()) }
        )
    }

    @Test
    fun timesOne() {
        assertEquals(Integer.ONE, Times(Integer.ONE, Integer.ONE).eval())

        // 1 * Sin(x) = Sin(x)
        val sinx = Sin(VarExpr("x"))
        assertEquals(sinx, Times(Integer.ONE, sinx).eval())
    }

    @Test
    fun doubleEval() {
        val e = Times(Integer.TWO, Integer.TWO)
        assertEquals(Integer(4), e.eval())
        assertEquals(Integer(4), e.eval())
    }

    @Test
    fun factorDuplicates() {
        val x = VarExpr("x")
        val y = VarExpr("y")
        assertEquals(Times(Integer(2), x), Times(x, x).eval())
        assertEquals(Times(Integer(3), x), Times(x, x, x).eval())
        assertEquals(Times(Integer(4), x, y), Times(x, x, y, y).eval())
    }

    @Test
    fun transforms() {
        // 2*(Pi/2 + Pi) = 3*Pi
        val e = Times(Integer.TWO,Plus(Pi(), Divide(Pi(), Integer.TWO))).eval()
        assertEquals(Times(Integer(3), Pi()), e)

        val x = RealBigDec("1.6475490")
        val y = RealBigDec("0.93267057")
        val z=  RealBigDec("0.3607293")
        // x * (y + iz)
        assertEquals(Complex("1.5366205", "0.5943192"), Times(x, y + Times(I(), z)).eval())
        assertEquals(Complex("1.5366205", "0.5943192"), Times(x, y) + Times(I(), x, z))
    }

    @Test
    fun transform_powerSameBase() {
        assertEquals(Integer.TWO,
                Power(Integer.TWO, Rational.HALF) * Power(Integer.TWO, Rational.HALF))
        assertEquals(Power(Integer(5), Rational(6,7)),
                Times(Power(Integer(5), Rational(1, 7)), Power(Integer(5), Rational(2,3)), Power(Integer(5), Rational(1,21))).eval())
    }

    @Test
    fun transform_powerSameExponent() {
        assertEquals(Power(Integer(6), Rational.HALF),
                Power(Integer.TWO, Rational.HALF) * Power(Integer(3), Rational.HALF))
        assertEquals(Power(Integer(30), Rational.THIRD),
                Times(Power(Integer(2), Rational.THIRD), Power(Integer(3), Rational.THIRD), Power(Integer(5), Rational.THIRD)).eval())
    }
}