package org.cerion.symcalc.expression.function.arithmetic

import expression.constant.I
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimesTest {

    @Test
    fun toString_Parenthesis() {
        assertEquals("2 * 3 * 4", Times(Integer.TWO, Integer(3), Integer(4)).toString())
        assertEquals("2 * (3 + 4)", Times(Integer.TWO, Plus(Integer(3), Integer(4))).toString())
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
    fun associativeProperty() {
        val inner = Times(VarExpr("x"), VarExpr("y"))
        val outer = Times(VarExpr("z"), inner)

        val e = outer.eval()
        assertTrue(e.isFunction("times"))
        assertEquals(3, e.size.toLong())
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
        assertEquals(Complex("1.5366205", "0.5943192"), Times(x, Plus(y, Times(I(), z))).eval())
        assertEquals(Complex("1.5366205", "0.5943192"), Plus(Times(x, y), Times(I(), x, z)).eval())
    }

    @Test
    fun powersCombined() {
        assertEquals(Integer.TWO, Times(Power(Integer.TWO, Rational.HALF), Power(Integer.TWO, Rational.HALF)).eval())
    }
}