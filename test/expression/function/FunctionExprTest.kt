package org.cerion.symcalc.expression.function

import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class FunctionExprTest {

    @Test
    fun equals() {
        val f1 = Plus(Integer(3), RealDouble(5.0))
        val f2 = Subtract(Integer(3), RealDouble(5.0))
        val f3 = Plus(Integer(4), RealDouble(5.0))
        val f4 = Plus(Integer(3), RealDouble(5.0)) // same as f1

        assertNotEquals(f1, f2)
        assertNotEquals(f1, f3)
        assertNotEquals(f2, f3)
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

        val e = outer.eval()
        assertTrue(e.isFunction("times"))
        kotlin.test.assertEquals(3, e.size.toLong())

        kotlin.test.assertEquals(Times(Integer(4), x, y), Times(Times(Integer.TWO, x), Times(Integer.TWO, y)).eval())
    }
}