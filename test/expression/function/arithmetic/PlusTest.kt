package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.assertEqual
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlusTest {

    @Test
    fun basicAddition() {
        val e = Plus(IntegerNum.ONE, IntegerNum.ONE)
        assertEquals(IntegerNum.TWO, e.eval())
    }

    @Test
    fun doubleEval() {
        val e = Plus(IntegerNum.ONE, IntegerNum.ONE)
        assertEqual(2, e.eval())
        assertEqual(2, e.eval())
    }

    @Test
    fun associativeProperty() {
        val inner = Plus(VarExpr("x"), VarExpr("y"))
        val outer = Plus(VarExpr("z"), inner)

        val e = outer.eval()
        assertTrue(e.isFunction("plus"))
        assertEquals(3, e.size.toLong())
    }

    @Test
    fun identityProperty() {
        val v = VarExpr("x")
        val n = IntegerNum(0)

        var e: Expr = Plus(v, n)
        assertEquals(v, e.eval())

        e = Plus(n, v)
        assertEquals(v, e.eval())
    }
}