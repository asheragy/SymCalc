package org.cerion.symcalc.function.special

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.number.RealBigDec
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll

internal class GammaTest {

    @Test
    fun realBigDec() {
        assertAll(
                Gamma(RealBigDec("2.222222222")) `==` "1.115367290"
        )
    }
}