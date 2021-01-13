package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class EulerETest {

    @Test
    fun oddZeros() {
        assertAll(
            EulerE(1) `==` 0,
            EulerE(3) `==` 0,
            EulerE(5) `==` 0,
            EulerE(13) `==` 0,
            EulerE(99999999) `==` 0,
        )
    }

    @Test
    fun evens() {
        assertAll(
            EulerE(0) `==` 1,
            EulerE(2) `==` -1,
            EulerE(4) `==` 5,
            EulerE(6) `==` -61,
            EulerE(8) `==` 1385,
            EulerE(10) `==` -50521
        )
    }
}