package org.cerion.symcalc.parser

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
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
    fun invalidParametercount() {
        assertTrue(Expr.parse("Sin(1,2)").isError)
    }
}