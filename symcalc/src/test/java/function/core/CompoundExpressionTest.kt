package org.cerion.symcalc.function.core

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.number.Integer
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

    @Test
    fun variableDoesNotOutliveEval() {
        // y = 3; x + y  ->  3 + x
        val body = Plus(VarExpr("x"), VarExpr("y"))
        CompoundExpression(Set(VarExpr("y"), Integer(3)), body) `==` Plus(Integer(3), VarExpr("x"))

        // A separate eval() of the same node must start from an empty scope
        body `==` Plus(VarExpr("x"), VarExpr("y"))
    }
}
