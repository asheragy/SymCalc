package org.cerion.symcalc.parser

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParserTest {

    @Test
    fun basic() {
        assertTrue(Expr.parse("1 + 2 + 3") is Plus)
        assertTrue(Expr.parse("1 * 2") is Times)
        assertTrue(Expr.parse("1 ^ 2") is Power)

        assertEquals(Pi(), Expr.parse("Pi"))
        assertEquals(RealDouble(3.44), Expr.parse("2.34 + 1.1").eval())
        assertEquals(Integer(3), Expr.parse("Plus(1,2)").eval())
    }

    @Test
    fun brackets() {
        assertEquals(Integer(14), Expr.parse("2 + (3 * 4)").eval())
        assertEquals(Integer(20), Expr.parse("(2 + 3) * 4").eval())
        assertEquals(Integer(20), Expr.parse("(((2 + 3) * 4))").eval())
    }

    @Test
    fun invalid_brackets() {
        assertTrue(Expr.parse("2 + (3 * 4").isError)
        assertTrue(Expr.parse("(2").isError)
        assertTrue(Expr.parse("(2+1))").isError)
        assertTrue(Expr.parse("Plus(1,2").isError)
    }

    @Test
    fun function() {
        assertEquals(Integer(120), Expr.parse("Factorial(5)").eval())
        assertEquals(RealDouble(1.6094379124341003), Expr.parse("Log(5.0)").eval())
        assertEquals(RealDouble(0.6989700043360189), Expr.parse("Log10(5.0)").eval())
    }

    @Test
    fun list() {
        assertEquals(ListExpr(1,2,3,4,5), Expr.parse("{1,2,3,4,5}"))
        assertEquals(Plus(ListExpr(1,2,3), VarExpr("x")), Expr.parse("{1,2,3} + x"))
        assertEquals(Plus(VarExpr("x"), ListExpr(1,2,3)), Expr.parse("x + {1,2,3}"))
    }

    @Test
    fun invalid_functionName() {
        //val e = Expr.parse("Asdf(5)")
        //assertTrue(e.isError)
    }

    @Test
    fun error_missingExpr() {
        assertTrue(Expr.parse("1+").isError)
        assertTrue(Expr.parse("1*").isError)
        assertTrue(Expr.parse("   ").isError)
    }

    @Test
    fun complexNumbers() {
        var e = Expr.parse("5i")
        assertEquals(Complex(0,5), e.eval())

        e = Expr.parse("2 + 5i")
        assertTrue(e is Plus)
        assertEquals(Complex(2,5), e.eval())
    }

    @Test
    fun factorial() {
        assertEquals(Integer(120), Expr.parse("5!").eval())
        assertEquals(Integer(121), Expr.parse("5! + 1").eval())
        assertEquals(Integer(124), Expr.parse("5! + 2 * 2").eval())

        assertEquals(Integer(121), Expr.parse("1 + 5!").eval())
        assertEquals(Integer(124), Expr.parse("2 * 2 + 5!").eval())

        assertEquals(Integer(729), Expr.parse("3^3!").eval())
        assertEquals(Integer(216), Expr.parse("3!^3").eval())
    }

    @Test
    fun invalidParametercount() {
        assertTrue(Expr.parse("Sin(1,2)").isError)
    }

    @Test
    fun continuedInput() {
        var expr = Expr.parse("1 / 3").eval()
        assertEquals(Rational(4,3), Expr.parse(expr, "+ 1").eval())

        expr = Expr.parse("4 / 3").eval()
        assertEquals(Integer(4), Expr.parse(expr, "* 3").eval())
    }
}