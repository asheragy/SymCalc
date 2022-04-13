package org.cerion.symcalc.function.special

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.number.RealDouble
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll

internal class PolyGammaTest {

    @Test
    fun double() {
        assertAll(
                PolyGamma(RealDouble(-3.3)) `==` RealDouble(3.620353460592126),
                PolyGamma(RealDouble(-3.0)) `==` ComplexInfinity(),
                PolyGamma(RealDouble(0.9)) `==` RealDouble(-0.7549269499470519),
                PolyGamma(RealDouble(1.1)) `==` RealDouble(-0.42375494041107653),
                PolyGamma(RealDouble(1.4616321449683622)) `==` RealDouble(0.0),
                PolyGamma(RealDouble(2.22222)) `==` RealDouble(0.5569454878848434),
                PolyGamma(RealDouble(5.0)) `==` RealDouble(1.5061176684318003)
        )
    }
}