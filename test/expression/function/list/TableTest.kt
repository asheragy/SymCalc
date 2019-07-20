package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.statistics.RandomInteger
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class TableTest {

    @Test
    fun validate() {
        // 0 parameters
        assertEquals(Expr.ExprType.ERROR, Table().eval().type)

        // 1 parameter
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2)).eval().type)

        // 3 parameters
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2), ListExpr(IntegerNum(1)), IntegerNum(3)).eval().type)

        // 2nd parameter is a list
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2), IntegerNum(3)).eval().type)
    }

    @Test
    fun validate_list() {
        // List must not be empty
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2), ListExpr()).eval().type)

        // If copies, first list parameter must be integer
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2), ListExpr(RationalNum(2, 3))).eval().type)

        // If 2+ parameters first must be a variable
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2), ListExpr(IntegerNum(2), IntegerNum(3))).eval().type)

        // if 2,3,4 parameters others must be integer
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2), ListExpr(VarExpr("a"), RationalNum(1, 2))).eval().type)
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2), ListExpr(VarExpr("a"), IntegerNum(5), Pi())).eval().type)
        assertEquals(Expr.ExprType.ERROR, Table(IntegerNum(2), ListExpr(VarExpr("a"), IntegerNum(5), IntegerNum(6), RealNum.create(2.34))).eval().type)
    }

    @Test
    fun copies() {
        // Table(3, 4) = {3,3,3,3}
        assertEquals(ListExpr(IntegerNum(3), IntegerNum(3), IntegerNum(3), IntegerNum(3)),
                Table(IntegerNum(3),
                        ListExpr(IntegerNum(4)))
                        .eval())

        // Table(Sin(x), 3) = {Sin(x), Sin(x), Sin(x)}
        assertEquals(ListExpr(Sin(VarExpr("x")), Sin(VarExpr("x")), Sin(VarExpr("x"))),
                Table(Sin(VarExpr("x")),
                        ListExpr(IntegerNum(3)))
                        .eval())

        // Table(2 + 5, 2) = {7,7}
        assertEquals(ListExpr(IntegerNum(7), IntegerNum(7)),
                Table(Plus(IntegerNum(2), IntegerNum(5)),
                        ListExpr(IntegerNum(2)))
                        .eval())
    }

    @Test
    fun range_oneToMax() {
        // Table(3, a, 5) = {3,3,3,3,3}
        assertEquals(ListExpr(IntegerNum(3), IntegerNum(3), IntegerNum(3), IntegerNum(3), IntegerNum(3)),
                Table(IntegerNum(3),
                        ListExpr(VarExpr("a"), IntegerNum(5)))
                        .eval())

        // Table(x, a, 5) = {x,x,x,x,x}
        assertEquals(ListExpr(VarExpr("x"), VarExpr("x"), VarExpr("x"), VarExpr("x"), VarExpr("x")),
                Table(VarExpr("x"),
                        ListExpr(VarExpr("a"), IntegerNum(5)))
                        .eval())

        // Table(a, a, 5) = {1,2,3,4,5}
        assertEquals(ListExpr(IntegerNum(1), IntegerNum(2), IntegerNum(3), IntegerNum(4), IntegerNum(5)),
                Table(VarExpr("a"),
                        ListExpr(VarExpr("a"), IntegerNum(5)))
                        .eval())
    }

    @Test
    fun range_minToMax() {
        // Table(3, a, 5, 7) = {3,3,3}
        assertEquals(ListExpr(IntegerNum(3), IntegerNum(3), IntegerNum(3)),
                Table(IntegerNum(3),
                        ListExpr(VarExpr("a"), IntegerNum(5), IntegerNum(7)))
                        .eval())

        // Table(a, a, 5, 7) = {5,6,7}
        assertEquals(ListExpr(IntegerNum(5), IntegerNum(6), IntegerNum(7)),
                Table(VarExpr("a"),
                        ListExpr(VarExpr("a"), IntegerNum(5), IntegerNum(7)))
                        .eval())
    }

    @Test
    fun range_minToMax_step() {
        // Table(a, a, 2, 10, 3) = {2,5,8}
        assertEquals(ListExpr(IntegerNum(2), IntegerNum(5), IntegerNum(8)),
                Table(VarExpr("a"),
                        ListExpr(VarExpr("a"), IntegerNum(2), IntegerNum(10), IntegerNum(3)))
                        .eval())
    }

    @Test
    fun range_values() {
        // Table(a, a, {3,6,1}} = {3,6,1}
        val values = ListExpr(IntegerNum(3), IntegerNum(6), IntegerNum(1))

        assertEquals(values,
                Table(VarExpr("a"), ListExpr(VarExpr("a"), values)).eval())
    }

    @Test
    fun exprDelayedEval() {
        val r = RandomInteger()
        val params = ListExpr(IntegerNum(20))
        var e: Expr = Table(r, params)

        e = e.eval()
        assertEquals(20, e.size.toLong())
        assertTrue(e[0].isInteger)
        var sum = IntegerNum.ZERO
        for (i in 0 until e.size)
            sum = sum.plus(e[i] as IntegerNum)

        //Result should be list of 0s and 1s, verify there is at least a few of each
        assertTrue(sum.intValue() > 2)
        assertTrue(sum.intValue() < 18)
    }
}