package org.cerion.symcalc

import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.function.special.Gamma
import org.cerion.symcalc.function.special.Zeta
import org.junit.Test

class Special {

    @Test
    fun gammaLarge() {
        val x = RealBigDec("3.14").increasePrecision(160)
        Gamma(x).eval()
    }

    @Test
    fun gammaMultiple() {
        val x = RealBigDec("3.14").increasePrecision(100)
        for(i in 0 until 5)
            Gamma(x).eval()
    }

    @Test
    fun zetaLarge() {
        val x = RealBigDec("3.14").increasePrecision(400)
        Zeta(x).eval()
    }

    @Test
    fun zetaMultiple() {
        val x = RealBigDec("3.14").increasePrecision(100)
        for(i in 0 until 20)
            Zeta(x).eval()
    }
}