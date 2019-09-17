package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BernoulliTest {

    @Test
    fun validate() {
        // Single integer parameter
        assertEquals(ErrorExpr::class, Bernoulli(RealDouble(2.5)).eval()::class)
        assertEquals(ErrorExpr::class, Bernoulli(Integer(1), Integer(2)).eval()::class)
    }

    @Test
    fun basic() {
        assertEquals(Integer(1), Bernoulli(Integer(0)).eval())
        assertEquals(Rational(-1,2), Bernoulli(Integer(1)).eval())
        assertEquals(Rational(1,6), Bernoulli(Integer(2)).eval())
        assertEquals(Integer.ZERO, Bernoulli(Integer(3)).eval())
        assertEquals(Rational(-1, 30), Bernoulli(Integer(4)).eval())

        // TODO fix speed on this
        assertEquals(Rational(-174611, 330), Bernoulli(Integer(20)).eval())
    }

}