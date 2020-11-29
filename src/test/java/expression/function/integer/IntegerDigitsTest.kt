package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class IntegerDigitsTest {

    @Test
    fun basic() {
        assertEquals(ListExpr(3, 3, 4, 5, 7), IntegerDigits(Integer(33457)).eval())
    }
}