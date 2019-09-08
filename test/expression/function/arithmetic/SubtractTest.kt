package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.Integer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubtractTest {

    @Test
    fun parser() {
        //Verify a-b = Subtract(a,b)
        val expr = Expr.parse("5-1")
        val e = expr.eval() as Integer
        assertEquals(4, e.intValue().toLong())
    }

    @Test
    fun invalidParameters() {
        var e = get()
        assertTrue(e.isError)

        e = get(Integer(5))
        assertTrue(e.isError)

        e = get(Integer(1), Integer(2), Integer(3))
        assertTrue(e.isError)

        //List sizes
        val a = Integer(0).toList(2)
        val b = Integer(0).toList(3)

        e = get(a, b)
        assertTrue(e.isError)
    }

    @Test
    fun basicNumbers() {

        var t = getI(Integer(5), Integer(3))
        assertEquals(2, t.intValue().toLong())

        t = getI(Integer(3), Integer(5))
        assertEquals(-2, t.intValue().toLong())
    }

    @Test
    fun nested() {
        var e: Expr = Subtract(Integer(1), Integer(2))
        e = Subtract(Integer(5), e)

        assertEquals(Integer(6), e.eval())
    }

    @Test
    fun identity() {
        val e = get(VarExpr("a"), Integer(0))
        assertTrue(VarExpr("a").equals(e))
    }

    @Test
    fun lists() {
        assertEquals(ListExpr(-6,3,3), Subtract(ListExpr(1,5,8), ListExpr(7,2,5)).eval())
        assertEquals(ListExpr(1,2,3), Subtract(ListExpr(3,4,5), Integer(2)).eval())
        assertEquals(ListExpr(7,6,5), Subtract(Integer(10), ListExpr(3,4,5)).eval())
    }

    // TODO remove this pattern and just use regular eval
    private operator fun get(vararg e: Expr): Expr {
        return Subtract(*e).eval()
    }

    private fun getI(vararg e: Expr): Integer {
        val t = Subtract(*e).eval()

        return t as Integer
    }
}
