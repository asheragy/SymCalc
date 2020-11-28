package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import kotlin.test.Test
import kotlin.test.assertEquals


class SqrtTest {

    @Test
    fun validate() {
        assertEquals(Expr.ExprType.ERROR, Sqrt().eval().type)
        assertEquals(Expr.ExprType.ERROR, Sqrt(Integer(1), Integer(2)).eval().type)
    }

    @Test
    fun basic() {
        // Converts to power
        assertEquals(Power(Integer(3), Rational(1, 2)), Sqrt(Integer(3)).eval())

        // Evaluates when square number
        assertEquals(Integer(3), Sqrt(Integer(9)).eval())
        assertEquals(Integer(4), Sqrt(Integer(16)).eval())
        assertEquals(Integer("985236412054"), Sqrt(Integer("970690787637039276498916")).eval())
    }

    @Test
    fun large() {
        val big = Rational(1,3).toPrecision(50)
        assertEquals(RealBigDec("0.57735026918962576450914878050195745564760175127013"), Sqrt(big).eval())
    }

    @Test
    fun reduces() {
        // 8 = 2*sqrt(2)
        assertEquals(Times(Integer(2), Power(Integer(2), Rational(1, 2))),
                Sqrt(Integer(8)).eval())

        // 497664 = 288 * sqrt(6)
        assertEquals(Times(Integer(288), Power(Integer(6), Rational(1, 2))),
                Sqrt(Integer(497664)).eval())
    }
}