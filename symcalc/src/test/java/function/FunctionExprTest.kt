package org.cerion.symcalc.function

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.arithmetic.Subtract
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.trig.Sin
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class FunctionExprTest {

    @Test
    fun equals() {
        val f1 = Plus(Integer(3), RealDouble(5.0))
        val f2 = Subtract(Integer(3), RealDouble(5.0))
        val f3 = Plus(Integer(4), RealDouble(5.0))
        val f4 = Plus(Integer(3), RealDouble(5.0)) // same as f1

        assertNotEquals(f1 as Expr, f2)
        assertNotEquals(f1, f3)
        assertNotEquals(f2 as Expr, f3)
        assertEquals(f1, f4)
    }

    @Test
    fun equals_order() {
        assertEquals(Divide(Integer(1), Integer(2)), Divide(Integer(1), Integer(2)))
        assertNotEquals(Divide(Integer(1), Integer(2)), Divide(Integer(2), Integer(1)))
    }

    @Test
    fun equals_OrderlessProperty() {
        assertEquals(Times(Integer(1), Integer(2), Integer(3)), Times(Integer(2), Integer(3), Integer(1)))
    }

    @Test
    fun flatProperty() {
        val x = VarExpr("x")
        val y = VarExpr("y")
        val inner = Times(x, y)
        val outer = Times(VarExpr("z"), inner)

        assertEquals(Times(x, y, VarExpr("z")), outer.eval())
        assertEquals(Times(Integer(4), x, y), Times(Integer.TWO, x) * Times(Integer.TWO, y))
    }

    @Test
    fun listableProperty() {
        val list = ListExpr(RealDouble(2.4), RealDouble(0.4), RealDouble(1.4))
        assertEquals(ListExpr(RealDouble(0.675463180551151), RealDouble(0.3894183423086505), RealDouble(0.9854497299884601)), Sin(list).eval())
    }

    @Test
    fun listable_differentSizes() {
        assertEquals(Expr.ExprType.ERROR, (ListExpr(1,2,3) + ListExpr(4,5)).type)
        assertEquals(Expr.ExprType.ERROR, (ListExpr(1,2) - ListExpr(4,5,6)).type)
    }

    @Test
    fun listable_scalarAndList() {
        // Two-argument case, already handled
        assertEquals(ListExpr(4, 5, 6), Plus(Integer(3), ListExpr(1, 2, 3)).eval())
        assertEquals(ListExpr(4, 5, 6), Plus(ListExpr(1, 2, 3), Integer(3)).eval())
    }

    @Test
    fun listable_threadsOverAnyArity() {
        // Requires general threadOverLists: current code only threads one- and two-argument calls
        assertEquals(ListExpr(8, 9), Plus(ListExpr(1, 2), Integer(3), Integer(4)).eval())
        assertEquals(ListExpr(9, 10), Plus(Integer(1), ListExpr(2, 3), Integer(6)).eval())
    }

    @Test
    fun listable_multipleListsAnyArity() {
        assertEquals(ListExpr(8, 9), Plus(ListExpr(1, 2), ListExpr(5, 5), Integer(2)).eval())
        assertEquals(Expr.ExprType.ERROR, Plus(ListExpr(1, 2), ListExpr(5, 5, 5), Integer(2)).eval().type)
    }

    @Test
    fun createInvalidParameterCount() {
        assertFailsWith<ArrayIndexOutOfBoundsException> { FunctionExpr.createFunction("Sin").eval() }
        assertFailsWith<IllegalArgumentException> { FunctionExpr.createFunction("Sin", Integer(2), Integer(3)).eval() }
    }

}
