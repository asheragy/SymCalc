package org.cerion.symcalc.function.calculus

import org.cerion.symcalc.`==`
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll

internal class SumTest {

    @Test
    fun basic() {
        assertAll(
                Sum("x", listOf("x", 0, 10)) `==` 55
        )
    }
}