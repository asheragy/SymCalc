package org.cerion.symcalc.parser

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Complex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ParserTest {

    @Test
    fun basic() {
        var e = Expr.parse("1 + 2 + 3")
        assertTrue(e is Plus)

        e = Expr.parse("1 * 2")
        assertTrue(e is Times)

        e = Expr.parse("1 ^ 2")
        assertTrue(e is Power)

        e = Expr.parse("Pi")
        assertTrue(e.isConst)

        e = Expr.parse("2.34 + 1.1").eval()
        assertEquals("3.44", e.toString())

        e = Expr.parse("Plus(1,2)").eval()
        assertEquals("3", e.toString())
    }

    @Test
    fun brackets() {
        var e = Expr.parse("2 + (3 * 4)")
        assertEquals("14", e.eval().toString())

        e = Expr.parse("(2 + 3) * 4")
        assertEquals("20", e.eval().toString())

        e = Expr.parse("(((2 + 3) * 4))")
        assertEquals("20", e.eval().toString())
    }

    @Test
    fun invalid_brackets() {
        var e = Expr.parse("2 + (3 * 4")
        assertTrue(e.isError)

        e = Expr.parse("(2")
        assertTrue(e.isError)

        // TODO check if this is error
        //e = Expr.parse("(2+1))")
        //assertTrue(e.isError)

        e = Expr.parse("Plus(1,2")
        assertTrue(e.isError)
    }

    @Test
    fun invalid_functionName() {
        //val e = Expr.parse("Asdf(5)")
        //assertTrue(e.isError)
    }

    @Test
    fun error_missingExpr() {
        var e = Expr.parse("1+")
        assertTrue(e.isError)

        e = Expr.parse("1*")
        assertTrue(e.isError)

        e = Expr.parse("   ")
        assertTrue(e.isError)
    }

    @Test
    fun complexNumbers() {
        var e = Expr.parse("5i")
        assertEquals(Complex(0,5), e.eval())

        e = Expr.parse("2 + 5i")
        assertTrue(e is Plus)
        assertEquals(Complex(2,5), e.eval())
    }
}