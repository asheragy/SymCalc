package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.assertEquals
import org.junit.Test

class CompoundExpressionTest {

    @Test
    fun variableScope() {
        val e1 = Set(VarExpr("x"), IntegerNum(5))
        val e2 = Plus(IntegerNum(5), VarExpr("x"))

        val e = CompoundExpression(e1, e2)
        assertEquals(IntegerNum(10), e.eval())
    }
}