package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class SubtractTest {

    @Test
    fun parser() {
        //Verify a-b = Subtract(a,b)
        val expr = Expr.parse("5-1")
        val e = expr.eval() as IntegerNum
        assertEquals(4, e.intValue().toLong())
    }

    @Test
    fun invalidParameters() {
        var e = get()
        assertTrue(e.isError)


        e = get(IntegerNum(5))
        assertTrue(e.isError)

        e = get(IntegerNum(1), IntegerNum(2), IntegerNum(3))
        assertTrue(e.isError)

        //List sizes
        val a = IntegerNum(0).toList(2)
        val b = IntegerNum(0).toList(3)

        e = get(a, b)
        assertTrue(e.isError)
    }

    @Test
    fun basicNumbers() {

        var t = getI(IntegerNum(5), IntegerNum(3))
        assertEquals(2, t.intValue().toLong())

        t = getI(IntegerNum(3), IntegerNum(5))
        assertEquals(-2, t.intValue().toLong())
    }

    @Test
    fun nested() {
        var e: Expr = Subtract(IntegerNum(1), IntegerNum(2))
        e = Subtract(IntegerNum(5), e)

        assertEquals(IntegerNum(6), e.eval())
    }

    @Test
    fun identity() {
        val e = get(VarExpr("a"), IntegerNum(0))

        assertTrue(VarExpr("a").equals(e))
    }

    @Test
    fun lists() {

        val a = IntegerNum(10).toList(5)
        val i = IntegerNum(3)

        //List - Single
        var e = get(a, i)
        assertTrue(e.isList)

        var l = e as ListExpr
        assertEquals(5, l.size.toLong())

        for (n in 0..4)
            assertEquals(7, (l[n] as IntegerNum).intValue().toLong())

        //Single - List
        e = get(i, a)

        l = e as ListExpr
        assertEquals(5, l.size.toLong())

        for (n in 0..4)
            assertEquals(-7, (l[n] as IntegerNum).intValue().toLong())
    }


    private operator fun get(vararg e: Expr): Expr {
        return Subtract(*e).eval()
    }

    private fun getI(vararg e: Expr): IntegerNum {
        val t = Subtract(*e).eval()

        return t as IntegerNum
    }
}
