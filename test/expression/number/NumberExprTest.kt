package expression.number

import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NumberExprTest {

    @Test
    fun equals_afterEval() {
        assertNotEquals(IntegerNum.ONE, ComplexNum(1, 0))
        assertEquals(IntegerNum.ONE, ComplexNum(1, 0).eval())

        assertNotEquals(IntegerNum.ONE, Rational(1,1))
        assertEquals(IntegerNum.ONE, Rational(1,1).eval())
    }
}