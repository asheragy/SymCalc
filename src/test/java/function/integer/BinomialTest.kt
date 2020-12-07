package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals

class BinomialTest {

    @Test
    fun basic() {
        assertEquals(Integer(120), Binomial(Integer(10), Integer(3)).eval())
    }
}