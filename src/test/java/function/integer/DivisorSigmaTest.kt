package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import kotlin.test.Test

internal class DivisorSigmaTest {

    @Test
    fun basic() {
        assertAll(
                DivisorSigma(2, 20) `==` 546
        )
    }
}