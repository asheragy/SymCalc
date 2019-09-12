package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NTest {

    @Test
    fun constantPlusInteger() {
        var e: Expr = N(Plus(Pi(), Integer(5)))
        assertEquals(RealDouble(8.141592653589793), e.eval())

        e = Plus(N(Integer(5)), Pi())
        assertEquals(RealDouble(8.141592653589793), e.eval())
    }

    @Test
    fun zeroPrecision() {
        // TODO this should work assertEquals(RealBigDec("0.0"), N(Pi(), Integer(0)).eval())
    }
}