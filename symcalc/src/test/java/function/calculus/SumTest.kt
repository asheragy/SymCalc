package org.cerion.symcalc.function.calculus

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.junit.Test

internal class SumTest {

    @Test
    fun basic() {
        assertAll(
                Sum("x", listOf("x", 0, 10)) `==` 55
        )
    }
}