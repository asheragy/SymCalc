package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import kotlin.test.Test
import kotlin.test.assertEquals

class MapTest {
    @Test
    fun basic() {
        val list = ListExpr(Integer.ZERO, Times(Pi(), Rational(1,2)), Pi(), Times(Pi(), Rational(3,2)))
        assertEquals(ListExpr(0, 1, 0, -1), Map(SymbolExpr("sin"), list).eval())
    }
}

