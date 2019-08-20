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

        val a = Integer(10).toList(5)
        val i = Integer(3)

        //List - Single
        var e = get(a, i)
        assertTrue(e.isList)

        var l = e as ListExpr
        assertEquals(5, l.size.toLong())

        for (n in 0..4)
            assertEquals(7, (l[n] as Integer).intValue().toLong())

        //Single - List
        e = get(i, a)

        l = e as ListExpr
        assertEquals(5, l.size.toLong())

        for (n in 0..4)
            assertEquals(-7, (l[n] as Integer).intValue().toLong())
    }


    private operator fun get(vararg e: Expr): Expr {
        return Subtract(*e).eval()
    }

    private fun getI(vararg e: Expr): Integer {
        val t = Subtract(*e).eval()

        return t as Integer
    }
}
