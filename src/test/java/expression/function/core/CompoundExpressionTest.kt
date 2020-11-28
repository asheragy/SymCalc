package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals

class CompoundExpressionTest {

    @Test
    fun variableScope() {
        val e1 = Set(VarExpr("x"), Integer(5))
        val e2 = Plus(Integer(5), VarExpr("x"))

        val e = CompoundExpression(e1, e2)
        assertEquals(Integer(10), e.eval())
    }
}