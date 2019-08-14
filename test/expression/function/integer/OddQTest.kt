package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum_Double
import org.junit.Test

import org.junit.Assert.*

class OddQTest {

    @Test
    fun invalidParameterCount() {
        var e: Expr = OddQ(IntegerNum.ONE, IntegerNum.TWO)
        assertTrue(e.eval().isError)

        e = OddQ()
        assertTrue(e.eval().isError)
    }

    @Test
    fun nonIntegerInput() {
        val e = ListExpr()
        val num = RealNum_Double(3.0)

        assertEquals(BoolExpr.FALSE, OddQ(e).eval())
        assertEquals(BoolExpr.FALSE, OddQ(num).eval())
    }

    @Test
    fun basicTest() {
        assertEquals(BoolExpr.FALSE, OddQ(IntegerNum.ZERO).eval())
        assertEquals(BoolExpr.TRUE, OddQ(IntegerNum.ONE).eval())
        assertEquals(BoolExpr.FALSE, OddQ(IntegerNum.TWO).eval())

        // negative
        assertEquals(BoolExpr.FALSE, OddQ(NumberExpr.parse("-34")).eval())
        assertEquals(BoolExpr.TRUE, OddQ(NumberExpr.parse("-35")).eval())

        // Large
        assertEquals(BoolExpr.TRUE, OddQ(NumberExpr.parse("1234567890987654321")).eval())
        assertEquals(BoolExpr.TRUE, OddQ(NumberExpr.parse("-1234567890987654321")).eval())
        assertEquals(BoolExpr.FALSE, OddQ(NumberExpr.parse("1234567890987654322")).eval())
        assertEquals(BoolExpr.FALSE, OddQ(NumberExpr.parse("-1234567890987654322")).eval())
    }
}