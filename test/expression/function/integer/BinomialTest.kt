package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.number.Integer
import org.junit.Assert.*
import org.junit.Test

class BinomialTest {

    @Test
    fun basic() {
        assertEquals(Integer(120), Binomial(Integer(10), Integer(3)).eval())
    }
}