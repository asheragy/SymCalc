package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.statistics.RandomInteger
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TableTest {

    @Test
    fun validate() {
        // 0 parameters
        assertEquals(Expr.ExprType.ERROR, Table().eval().type)

        // 1 parameter
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2)).eval().type)

        // 3 parameters
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2), ListExpr(Integer(1)), Integer(3)).eval().type)

        // 2nd parameter is a list
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2), Integer(3)).eval().type)
    }

    @Test
    fun validate_list() {
        // List must not be empty
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2), ListExpr()).eval().type)

        // If copies, first list parameter must be integer
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2), ListExpr(Rational(2, 3))).eval().type)

        // If 2+ parameters first must be a variable
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2), ListExpr(Integer(2), Integer(3))).eval().type)

        // if 2,3,4 parameters others must be integer
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2), ListExpr(VarExpr("a"), Rational(1, 2))).eval().type)
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2), ListExpr(VarExpr("a"), Integer(5), Pi())).eval().type)
        assertEquals(Expr.ExprType.ERROR, Table(Integer(2), ListExpr(VarExpr("a"), Integer(5), Integer(6), RealDouble(2.34))).eval().type)
    }

    @Test
    fun copies() {
        // Table(3, 4) = {3,3,3,3}
        assertEquals(ListExpr(Integer(3), Integer(3), Integer(3), Integer(3)),
                Table(Integer(3),
                        ListExpr(Integer(4)))
                        .eval())

        // Table(Sin(x), 3) = {Sin(x), Sin(x), Sin(x)}
        assertEquals(ListExpr(Sin(VarExpr("x")), Sin(VarExpr("x")), Sin(VarExpr("x"))),
                Table(Sin(VarExpr("x")),
                        ListExpr(Integer(3)))
                        .eval())

        // Table(2 + 5, 2) = {7,7}
        assertEquals(ListExpr(Integer(7), Integer(7)),
                Table(Plus(Integer(2), Integer(5)),
                        ListExpr(Integer(2)))
                        .eval())
    }

    @Test
    fun range_oneToMax() {
        // Table(3, a, 5) = {3,3,3,3,3}
        assertEquals(ListExpr(Integer(3), Integer(3), Integer(3), Integer(3), Integer(3)),
                Table(Integer(3),
                        ListExpr(VarExpr("a"), Integer(5)))
                        .eval())

        // Table(x, a, 5) = {x,x,x,x,x}
        assertEquals(ListExpr(VarExpr("x"), VarExpr("x"), VarExpr("x"), VarExpr("x"), VarExpr("x")),
                Table(VarExpr("x"),
                        ListExpr(VarExpr("a"), Integer(5)))
                        .eval())

        // Table(a, a, 5) = {1,2,3,4,5}
        assertEquals(ListExpr(Integer(1), Integer(2), Integer(3), Integer(4), Integer(5)),
                Table(VarExpr("a"),
                        ListExpr(VarExpr("a"), Integer(5)))
                        .eval())
    }

    @Test
    fun range_minToMax() {
        // Table(3, a, 5, 7) = {3,3,3}
        assertEquals(ListExpr(Integer(3), Integer(3), Integer(3)),
                Table(Integer(3),
                        ListExpr(VarExpr("a"), Integer(5), Integer(7)))
                        .eval())

        // Table(a, a, 5, 7) = {5,6,7}
        assertEquals(ListExpr(Integer(5), Integer(6), Integer(7)),
                Table(VarExpr("a"),
                        ListExpr(VarExpr("a"), Integer(5), Integer(7)))
                        .eval())
    }

    @Test
    fun range_minToMax_step() {
        // Table(a, a, 2, 10, 3) = {2,5,8}
        assertEquals(ListExpr(Integer(2), Integer(5), Integer(8)),
                Table(VarExpr("a"),
                        ListExpr(VarExpr("a"), Integer(2), Integer(10), Integer(3)))
                        .eval())
    }

    @Test
    fun range_values() {
        // Table(a, a, {3,6,1}} = {3,6,1}
        val values = ListExpr(Integer(3), Integer(6), Integer(1))

        assertEquals(values,
                Table(VarExpr("a"), ListExpr(VarExpr("a"), values)).eval())
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