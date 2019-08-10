package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimesTest {

    @Test
    fun toString_Parenthesis() {
        assertEquals("2 * 3 * 4", Times(IntegerNum.TWO, IntegerNum(3), IntegerNum(4)).toString())
        assertEquals("2 * (3 + 4)", Times(IntegerNum.TWO, Plus(IntegerNum(3), IntegerNum(4))).toString())
    }

    @Test
    fun timesOne() {
        // 1 * Sin(x) = Sin(x)
        val sinx = Sin(VarExpr("x"))
        assertEquals(sinx, Times(IntegerNum.ONE, sinx).eval())
    }

    @Test
    fun doubleEval() {
        val e = Times(IntegerNum.TWO, IntegerNum.TWO)
        assertEquals(IntegerNum(4), e.eval())
        assertEquals(IntegerNum(4), e.eval())
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
        val e = Times(IntegerNum.TWO,Plus(Pi(), Divide(Pi(), IntegerNum.TWO))).eval()
        assertEquals(Times(IntegerNum(3), Pi()), e)
    }

    @Test
    fun powersCombined() {
        assertEquals(IntegerNum.TWO, Times(Power(IntegerNum.TWO, Rational.HALF), Power(IntegerNum.TWO, Rational.HALF)).eval())
    }
}