package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EvenQTest {
    @Test
    fun invalidParameterCount() {
        var e: Expr = EvenQ(Integer.ONE, Integer.TWO)
        assertTrue(e.eval().isError)

        e = EvenQ()
        assertTrue(e.eval().isError)
    }

    @Test
    fun nonIntegerInput() {
        val e = ListExpr()
        val num = RealDouble(3.0)

        assertEquals(BoolExpr.FALSE, EvenQ(e).eval())
        assertEquals(BoolExpr.FALSE, EvenQ(num).eval())
    }

    @Test
    fun basicTest() {
        assertEquals(BoolExpr.TRUE, EvenQ(Integer.ZERO).eval())
        assertEquals(BoolExpr.FALSE, EvenQ(Integer.ONE).eval())
        assertEquals(BoolExpr.TRUE, EvenQ(Integer.TWO).eval())

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