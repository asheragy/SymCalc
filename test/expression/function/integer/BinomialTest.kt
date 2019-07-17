package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.*
import org.junit.Test

class BinomialTest {

    @Test
    fun basic() {
        assertEquals(IntegerNum(120), Binomial(IntegerNum(10), IntegerNum(3)).eval())
    }
}