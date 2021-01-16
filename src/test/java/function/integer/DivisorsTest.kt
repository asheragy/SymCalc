package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import kotlin.test.Test

internal class DivisorsTest {

    @Test
    fun basic() {
        assertAll(
                Divisors(20) `==` listOf(1, 2, 4, 5, 10, 20)
        )
    }
}