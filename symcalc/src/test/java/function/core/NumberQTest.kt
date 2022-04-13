package org.cerion.symcalc.function.core

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NumberQTest {

    @Test
    fun validation() {
        assertTrue(NumberQ().eval().isError)
        assertTrue(NumberQ(Integer(5), Integer(5)).eval().isError)
    }

    @Test
    fun basic() {
        assertEquals(BoolExpr.TRUE, NumberQ(Integer(5)).eval())
        assertEquals(BoolExpr.TRUE, NumberQ(RealDouble(2.34)).eval())
        assertEquals(BoolExpr.TRUE, NumberQ(Rational(2, 3)).eval())
        assertEquals(BoolExpr.TRUE, NumberQ(Complex(5, 7)).eval())

        // Constant is not
        assertEquals(BoolExpr.FALSE, NumberQ(Pi()).eval())

        // Variable is not
        assertEquals(BoolExpr.FALSE, NumberQ(VarExpr("x")).eval())

        // Unless assigned to something that is NumberQ=true
        val e = CompoundExpression(
                Set(VarExpr("x"), Integer(5)),
                NumberQ(VarExpr("x")))

        assertEquals(BoolExpr.TRUE, e.eval())
    }
}