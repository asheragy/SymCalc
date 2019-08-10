package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.assertEqual
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlusTest {

    @Test
    fun basicAddition() {
        assertEquals(IntegerNum.TWO, Plus(IntegerNum.ONE, IntegerNum.ONE).eval())
        assertEquals(IntegerNum(101), Plus(IntegerNum.ONE, IntegerNum.ONE, IntegerNum(99)).eval())
        assertEquals(Rational(-1,2), Plus(IntegerNum.ONE, Rational(1,2), IntegerNum.ONE, IntegerNum(-3)).eval())
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

    @Test
    fun toStringTest() {
        assertEquals("5 + 3.14 + E", Plus(IntegerNum(5), RealNum.create(3.14), E()).toString())
    }

    @Test
    fun multipleTerms_Times() {
        assertEquals(Times(Pi(), IntegerNum.TWO), Plus(Pi(), Pi()).eval())
    }

    @Test
    fun nestedTimes() {
        assertEquals(Times(IntegerNum(3), Pi()), Plus(Times(IntegerNum(2), Pi()), Pi()).eval())
    }
}