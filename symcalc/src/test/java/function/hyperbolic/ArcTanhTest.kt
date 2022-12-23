package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.number.RealBigDec
import org.junit.Test

internal class ArcTanhTest {

    @Test
    fun exact() {
        ArcTanh(-1) `==` Minus(Infinity())
        ArcTanh(1) `==` Infinity()
    }

    @Test
    fun bigDecimal() {
        ArcTanh(RealBigDec("0.5", 50)) `==` "0.54930614433405484569762261846126285232374527891137"
    }
}