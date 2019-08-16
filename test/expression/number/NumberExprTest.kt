package expression.number

import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberTestBase
import org.cerion.symcalc.expression.number.Rational
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NumberExprTest : NumberTestBase() {

    @Test
    fun equals_afterEval() {
        assertNotEquals(Integer.ONE, Complex(1, 0))
        assertEquals(Integer.ONE, Complex(1, 0).eval())

        assertNotEquals(Integer.ONE, Rational(1,1))
        assertEquals(Integer.ONE, Rational(1,1).eval())
    }
}