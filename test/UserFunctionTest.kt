package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.assertEquals

class UserFunctionTest {

    @Test
    fun basic_substitution() {
        val uf = UserFunction("foo", VarExpr("x"), VarExpr("x"))

        // Same input
        var input: Expr = VarExpr("x")
        var output = uf.eval(input)
        assertEquals(input, output)

        // Number
        input = IntegerNum(5)
        output = uf.eval(input)
        assertEquals(input, output)

        // Another variable
        input = VarExpr("y")
        output = uf.eval(input)
        assertEquals(input, output)
    }

    @Test
    fun addition() {
        val e = Plus(VarExpr("x"), VarExpr("x"))
        val uf = UserFunction("foo", e, VarExpr("x"))

        var input: Expr = IntegerNum(5)
        var output = uf.eval(input)
        assertEquals(IntegerNum(10), output)

        input = IntegerNum(1)
        output = uf.eval(input)
        assertEquals(IntegerNum(2), output)
    }
}