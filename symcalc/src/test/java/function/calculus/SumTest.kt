package org.cerion.symcalc.function.calculus

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.core.CompoundExpression
import org.junit.Test

internal class SumTest {

    @Test
    fun basic() {
        Sum("x", listOf("x", 0, 10)) `==` 55
    }

    @Test
    fun iteratorDoesNotLeak() {
        // Sum[x, {x, 0, 10}]; x  ->  x
        CompoundExpression(Sum("x", listOf("x", 0, 10)), VarExpr("x")) `==` VarExpr("x")
    }
}
