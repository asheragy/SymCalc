package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals

class PlusTest {

    @Test
    fun basicAddition() {
        assertEquals(Integer.TWO, Plus(Integer.ONE, Integer.ONE).eval())
        assertEquals(Integer(101), Plus(Integer.ONE, Integer.ONE, Integer(99)).eval())
        assertEquals(Rational(-1,2), Plus(Integer.ONE, Rational(1,2), Integer.ONE, Integer(-3)).eval())
    }

    @Test
    fun doubleEval() {
        val e = Plus(Integer.ONE, Integer.ONE)
        assertEquals(Integer(2), e.eval())
        assertEquals(Integer(2), e.eval())
    }

    @Test
    fun associativeProperty() {
        val inner = Plus(VarExpr("x"), VarExpr("y"))
        val outer = Plus(VarExpr("z"), inner)

        val e = outer.eval()
        assertEquals(Plus(VarExpr("x"), VarExpr("y"), VarExpr("z")), e)
    }

    @Test
    fun identityProperty() {
        val v = VarExpr("x")
        val n = Integer(0)

        var e: Expr = Plus(v, n)
        assertEquals(v, e.eval())

        e = Plus(n, v)
        assertEquals(v, e.eval())
    }

    @Test
    fun toStringTest() {
        assertEquals("5 + 3.14 + E", Plus(Integer(5), RealDouble(3.14), E()).toString())
    }

    @Test
    fun multipleTerms_Times() {
        assertEquals(Times(Pi(), Integer.TWO), Pi() + Pi())
    }

    @Test
    fun nestedTimes() {
        assertEquals(Times(Integer(3), Pi()), (Integer(2) * Pi()) + Pi())
    }
}