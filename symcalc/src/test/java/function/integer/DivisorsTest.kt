package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import kotlin.test.Test

internal class DivisorsTest {

    @Test
    fun basic() {
        assertAll(
                Divisors(19) `==` listOf(1, 19),
                Divisors(4) `==` listOf(1, 2, 4),
                Divisors(20) `==` listOf(1, 2, 4, 5, 10, 20),
                Divisors(512) `==` listOf(1, 2, 4, 8, 16, 32, 64, 128, 256, 512)
        )
    }
}