package expression.number

import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberTestBase
import org.cerion.symcalc.expression.number.Rational
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NumberExprTest : NumberTestBase() {

    @Test
    fun equals_afterEval() {
        assertNotEquals(IntegerNum.ONE, Complex(1, 0))
        assertEquals(IntegerNum.ONE, Complex(1, 0).eval())

        assertNotEquals(IntegerNum.ONE, Rational(1,1))
        assertEquals(IntegerNum.ONE, Rational(1,1).eval())
    }
}