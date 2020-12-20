package org.cerion.symcalc.function.list

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.statistics.RandomInteger
import org.cerion.symcalc.function.trig.Sin
import org.junit.jupiter.api.assertAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TableTest {

    @Test
    fun validate() {
        // 0 parameters
        assertEquals(Expr.ExprType.ERROR, Table().eval().type)

        // 1 parameter
        assertEquals(Expr.ExprType.ERROR, Table(2).eval().type)

        // 3 parameters
        assertEquals(Expr.ExprType.ERROR, Table(2, ListExpr(1), 3).eval().type)

        // 2nd parameter is a list
        assertEquals(Expr.ExprType.ERROR, Table(2, 3).eval().type)
    }

    @Test
    fun validate_list() {
        // List must not be empty
        assertEquals(Expr.ExprType.ERROR, Table(2, ListExpr()).eval().type)

        // If copies, first list parameter must be integer
        assertEquals(Expr.ExprType.ERROR, Table(2, ListExpr(Rational(2, 3))).eval().type)

        // If 2+ parameters first must be a variable
        assertEquals(Expr.ExprType.ERROR, Table(2, ListExpr(2, 3)).eval().type)

        // if 2,3,4 parameters others must be integer
        assertEquals(Expr.ExprType.ERROR, Table(2, ListExpr("a", Rational(1, 2))).eval().type)
        assertEquals(Expr.ExprType.ERROR, Table(2, ListExpr("a", 5, Pi())).eval().type)
        assertEquals(Expr.ExprType.ERROR, Table(2, ListExpr("a", 5, 6, 2.34)).eval().type)
    }

    @Test
    fun copies() {
        assertAll(
                Table(3, listOf(4)) `==` ListExpr(3, 3, 3, 3),
                Table(Sin("x"), listOf(3)) `==` ListExpr(Sin("x"), Sin("x"), Sin("x")),
                Table(Plus(2, 5), listOf(2)) `==` ListExpr(7, 7))
    }

    @Test
    fun range_oneToMax() {
        assertAll(
                Table(3, listOf("a", 5)) `==` ListExpr(3,3,3,3,3),
                Table("x", listOf("a", 5)) `==` ListExpr("x", "x", "x", "x", "x"),
                Table("a", listOf("a", 5)) `==` ListExpr(1, 2, 3, 4, 5))
    }

    @Test
    fun range_minToMax() {
        // Table(3, a, 5, 7) = {3,3,3}
        assertEquals(ListExpr(3, 3, 3), Table(3, ListExpr("a", 5, 7)).eval())

        // Table(a, a, 5, 7) = {5,6,7}
        assertEquals(ListExpr(5, 6, 7), Table("a", ListExpr("a", 5, 7)).eval())
    }

    @Test
    fun range_minToMax_step() {
        // Table(a, a, 2, 10, 3) = {2,5,8}
        assertEquals(ListExpr(2, 5, 8), Table("a", ListExpr("a", 2, 10, 3)).eval())
    }

    @Test
    fun range_values() {
        // Table(a, a, {3,6,1}} = {3,6,1}
        val values = ListExpr(3, 6, 1)

        assertEquals(values, Table("a", ListExpr("a", values)).eval())
    }

    @Test
    fun exprDelayedEval() {
        val r = RandomInteger()
        val params = ListExpr(Integer(20))
        var e: Expr = Table(r, params)

        e = e.eval() as ListExpr
        assertEquals(20, e.size.toLong())
        assertTrue(e[0].isInteger)
        var sum = Integer.ZERO
        for (i in 0 until e.size)
            sum = sum.plus(e[i] as Integer)

        //Result should be list of 0s and 1s, verify there is at least a few of each
        assertTrue(sum.intValue() > 2)
        assertTrue(sum.intValue() < 18)
    }
}