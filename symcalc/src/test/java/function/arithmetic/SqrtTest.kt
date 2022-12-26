package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
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
        Sqrt(3) `==` Power(3, Rational(1, 2))

        // Evaluates when square number
        Sqrt(9) `==` 3
        Sqrt(16) `==` 4
        Sqrt(Integer("970690787637039276498916")) `==` Integer("985236412054")
    }

    @Test
    fun large() {
        val big = Rational(1,3).toPrecision(50)
        Sqrt(big) `==` "0.57735026918962576450914878050195745564760175127013"
    }

    @Test
    fun reduces() {
        // 8 = 2*sqrt(2)
        Sqrt(8) `==` Times(2, Power(2, Rational(1, 2)))

        // 497664 = 288 * sqrt(6)
        Sqrt(497664) `==` Times(288, Power(6, Rational(1, 2)))
    }

    @Test
    fun infinity() {
        Sqrt(Infinity()) `==` Infinity()
        // TODO should actually be Infinity(Complex(0, 1))
        Sqrt(Infinity(-1)) `==` I() * Infinity()
    }
}