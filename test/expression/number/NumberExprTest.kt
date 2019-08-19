package expression.number

import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class NumberExprTest : NumberTestBase() {

    @Test
    fun equals_afterEval() {
        assertNotEquals(Integer.ONE as NumberExpr, Complex(1, 0) as NumberExpr)
        assertEquals(Integer.ONE, Complex(1, 0).eval())

        assertNotEquals(Integer.ONE as NumberExpr, Rational(1,1) as NumberExpr)
        assertEquals(Integer.ONE, Rational(1,1).eval())
    }
}