package org.cerion.symcalc.function.special

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll

internal class PolyGammaTest {

    @Test
    fun realBigDec() {

        assertAll(
                PolyGamma(RealDouble(2.22222)) `==` RealDouble(0.5569454878848434)
        )
    }
}