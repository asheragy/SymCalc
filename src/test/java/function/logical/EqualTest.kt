package org.cerion.symcalc.function.logical

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.number.*
import kotlin.test.Test
import kotlin.test.assertTrue

class EqualTest {

    @Test
    fun numbers() {
        val numbers = arrayOf(Integer(2), Rational(4,2), RealDouble(2.0), Complex(2,0))

        for(n1 in numbers) {
            for (n2 in numbers)
                isEqual(n1, n2)
        }
    }

    @Test
    fun precision_evaluatedFirst() {
        isEqual(Pi(), N(Pi()))
    }

    private fun isEqual(a: Expr, b: Expr) {
        val equal = Equal(a, b).eval()
        assertTrue(equal.asBool().value, "$a == $b")
    }
}