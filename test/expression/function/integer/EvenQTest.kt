package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.cerion.symcalc.expression.number.RealNum_Double
import org.junit.Test

import org.junit.Assert.*

class EvenQTest {
    @Test
    fun invalidParameterCount() {
        var e: Expr = EvenQ(IntegerNum.ONE, IntegerNum.TWO)
        assertTrue(e.eval().isError)

        e = EvenQ()
        assertTrue(e.eval().isError)
    }

    @Test
    fun nonIntegerInput() {
        val e = ListExpr()
        val num = RealNum_Double(3.0)

        assertEquals(BoolExpr.FALSE, EvenQ(e).eval())
        assertEquals(BoolExpr.FALSE, EvenQ(num).eval())
    }

    @Test
    fun basicTest() {
        assertEquals(BoolExpr.TRUE, EvenQ(IntegerNum.ZERO).eval())
        assertEquals(BoolExpr.FALSE, EvenQ(IntegerNum.ONE).eval())
        assertEquals(BoolExpr.TRUE, EvenQ(IntegerNum.TWO).eval())

        // negative
        assertEquals(BoolExpr.TRUE, EvenQ(NumberExpr.parse("-34")).eval())
        assertEquals(BoolExpr.FALSE, EvenQ(NumberExpr.parse("-35")).eval())

        // Large
        assertEquals(BoolExpr.FALSE, EvenQ(NumberExpr.parse("1234567890987654321")).eval())
        assertEquals(BoolExpr.FALSE, EvenQ(NumberExpr.parse("-1234567890987654321")).eval())
        assertEquals(BoolExpr.TRUE, EvenQ(NumberExpr.parse("1234567890987654322")).eval())
        assertEquals(BoolExpr.TRUE, EvenQ(NumberExpr.parse("-1234567890987654322")).eval())
    }
}