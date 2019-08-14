package org.cerion.symcalc.expression.function

import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum_Double
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class FunctionExprTest {
    @Test
    fun equals() {
        val f1 = Plus(IntegerNum(3), RealNum_Double(5.0))
        val f2 = Subtract(IntegerNum(3), RealNum_Double(5.0))
        val f3 = Plus(IntegerNum(4), RealNum_Double(5.0))
        val f4 = Plus(IntegerNum(3), RealNum_Double(5.0)) // same as f1

        assertNotEquals(f1, f2)
        assertNotEquals(f1, f3)
        assertNotEquals(f2, f3)
        assertEquals(f1, f4)
    }

    @Test
    fun equals_order() {
        assertEquals(Divide(IntegerNum(1), IntegerNum(2)), Divide(IntegerNum(1), IntegerNum(2)))
        assertNotEquals(Divide(IntegerNum(1), IntegerNum(2)), Divide(IntegerNum(2), IntegerNum(1)))
    }

    @Test
    fun equals_OrderlessProperty() {
        assertEquals(Times(IntegerNum(1), IntegerNum(2), IntegerNum(3)), Times(IntegerNum(2), IntegerNum(3), IntegerNum(1)))
    }
}