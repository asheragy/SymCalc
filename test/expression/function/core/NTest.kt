package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum_Double
import org.junit.Assert.assertEquals
import org.junit.Test

class NTest {

    @Test
    fun constantPlusInteger() {
        var e: Expr = N(Plus(Pi(), IntegerNum(5)))
        assertEquals(RealNum_Double(8.141592653589793), e.eval())

        e = Plus(N(IntegerNum(5)), Pi())
        assertEquals(RealNum_Double(8.141592653589793), e.eval())
    }
}