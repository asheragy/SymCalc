package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.Expr.ExprType
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.Integer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SubtractTest {

    @Test
    fun parser() {
        val expr = Expr.parse("5-1")
        assertEquals(Integer(4), expr.eval())
    }

    @Test
    fun invalidParameters() {
        assertEquals(ExprType.ERROR, Subtract().eval().type)
        assertEquals(ExprType.ERROR, Subtract(Integer(5)).eval().type)
        assertEquals(ExprType.ERROR, Subtract(Integer(1), Integer(2), Integer(3)).eval().type)
    }

    @Test
    fun basicNumbers() {
        assertEquals(Integer(2), Subtract(Integer(5), Integer(3)).eval())
        assertEquals(Integer(-2), Subtract(Integer(3), Integer(5)).eval())
    }

    @Test
    fun nested() {
        var e: Expr = Subtract(Integer(1), Integer(2))
        e = Subtract(Integer(5), e)

        assertEquals(Integer(6), e.eval())
    }

    @Test
    fun identity() {
        assertEquals(VarExpr("a"), Subtract(VarExpr("a"), Integer(0)).eval())
    }

    @Test
    fun lists() {
        assertEquals(ListExpr(-6,3,3), Subtract(ListExpr(1,5,8), ListExpr(7,2,5)).eval())
        assertEquals(ListExpr(1,2,3), Subtract(ListExpr(3,4,5), Integer(2)).eval())
        assertEquals(ListExpr(7,6,5), Subtract(Integer(10), ListExpr(3,4,5)).eval())
    }
}
