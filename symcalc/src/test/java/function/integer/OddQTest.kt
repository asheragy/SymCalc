package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class OddQTest {

    @Test
    fun invalidParameterCount() {
        var e: Expr = OddQ(Integer.ONE, Integer.TWO)
        assertTrue(e.eval().isError)

        e = OddQ()
        assertTrue(e.eval().isError)
    }

    @Test
    fun nonIntegerInput() {
        val e = ListExpr()
        val num = RealDouble(3.0)

        assertEquals(BoolExpr.FALSE, OddQ(e).eval())
        assertEquals(BoolExpr.FALSE, OddQ(num).eval())
    }

    @Test
    fun basicTest() {
        assertEquals(BoolExpr.FALSE, OddQ(Integer.ZERO).eval())
        assertEquals(BoolExpr.TRUE, OddQ(Integer.ONE).eval())
        assertEquals(BoolExpr.FALSE, OddQ(Integer.TWO).eval())

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