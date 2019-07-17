package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Test

import org.junit.Assert.*

class NTest {

    @Test
    fun constantPlusInteger() {
        var e: Expr = N(Plus(Pi(), IntegerNum(5)))
        assertEquals(RealNum.create(8.141592653589793), e.eval())

        // TODO this is what mathematica does but it doesn't seem right
        e = Plus(N(IntegerNum(5)), Pi())
        assertEquals(RealNum.create(8.141592653589793), e.eval())
    }
}