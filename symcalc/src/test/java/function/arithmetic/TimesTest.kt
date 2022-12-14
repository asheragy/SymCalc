package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.trig.Cos
import org.cerion.symcalc.function.trig.Sin
import org.cerion.symcalc.number.*
import kotlin.test.Test
import kotlin.test.assertEquals

private val x = VarExpr("x")
private val y = VarExpr("y")

class TimesTest {

    @Test
    fun toString_Parenthesis() {
        assertEquals("2 * 3 * 4", Times(2, 3, 4).toString())
        assertEquals("2 * (3 + 4)", Times(2, Plus(3, 4)).toString())
    }

    @Test
    fun timesIntegerZero() {
        Times(5, 0) `==` 0
        Times(Rational(1,3), 0) `==` 0
        Times(RealDouble(3.14), 0) `==` 0
        Times(RealBigDec("3.14"), 0) `==` 0
        Times(Complex(2.0, Rational.HALF), 0) `==` 0
        Times(Pi(), Integer.ZERO) `==` 0
    }

    @Test
    fun timesRealZero() {
        Times(Integer(5), RealDouble()) `==` RealDouble()
        Times(Rational(1,3), RealDouble()) `==` RealDouble()
        Times(RealDouble(3.14), RealDouble()) `==` RealDouble()
        Times(RealBigDec("3.14"), RealDouble()) `==` RealDouble()
        Times(Complex(2.0, Rational.HALF), RealDouble()) `==` RealDouble()
        Times(Pi(), RealDouble()) `==` RealDouble()
    }

    @Test
    fun timesOne() {
        Times(1, 1) `==` 1
        Times(1, Sin(x)) `==` Sin(x)
    }

    @Test
    fun doubleEval() {
        val e = Times(Integer.TWO, Integer.TWO)
        assertEquals(Integer(4), e.eval())
        assertEquals(Integer(4), e.eval())
    }

    @Test
    fun factorDuplicates() {
        Times(x, x) `==` Power(x, 2)
        Times(x, x, x) `==` Power(x, 3)
        Times(x, x, y, y) `==` Times(Power(x, 2), Power(y, 2))
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
        Power(2, Rational.HALF) * Power(2, Rational.HALF) `==` 2
        Power(5, Rational(1, 7)) * Power(5, Rational(2,3)) * Power(5, Rational(1,21)) `==` Power(5, Rational(6,7))
    }

    @Test
    fun transform_powerSameExponent() {
        Power(2, Rational.HALF) * Power(3, Rational.HALF) `==` Power(6, Rational.HALF)
        Power(2, Rational.THIRD) * Power(3, Rational.THIRD) * Power(5, Rational.THIRD) `==` Power(30, Rational.THIRD)
    }

    @Test
    fun commonTermsPowered() {
        x * x `==` Power(x, 2)
        Times(x, x, x, y, y) `==` Power(x, 3) * Power(y, 2)
        Times(Pi(), Pi()) `==` Power(Pi(), 2)
        Times(x, Power(x, 2)) `==` Power(x, 3)
        Times(Cos(x), Cos(x)) `==` Power(Cos(x), 2)
    }
}