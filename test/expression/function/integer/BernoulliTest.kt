package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Assert.*
import org.junit.Test

class BernoulliTest {

    @Test
    fun validate() {
        // Single integer parameter
        assertEquals(ErrorExpr::class, Bernoulli(RealDouble(2.5)).eval()::class)
        assertEquals(ErrorExpr::class, Bernoulli(IntegerNum(1), IntegerNum(2)).eval()::class)
    }

    @Test
    fun basic() {
        assertEquals(IntegerNum(1), Bernoulli(IntegerNum(0)).eval())
        assertEquals(Rational(-1,2), Bernoulli(IntegerNum(1)).eval())
        assertEquals(Rational(1,6), Bernoulli(IntegerNum(2)).eval())
        assertEquals(IntegerNum.ZERO, Bernoulli(IntegerNum(3)).eval())
        assertEquals(Rational(-1, 30), Bernoulli(IntegerNum(4)).eval())
    }

}