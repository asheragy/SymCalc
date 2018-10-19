package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExprTest {

    @Test
    fun isInteger() {
        assertTrue(IntegerNum(5).isInteger)
        assertFalse(RealNum.create(5.0).isInteger)
        assertFalse(RationalNum(1,2).isInteger)
        assertFalse(ComplexNum(1,1).isInteger)

        assertFalse(ListExpr().isInteger)
    }

}