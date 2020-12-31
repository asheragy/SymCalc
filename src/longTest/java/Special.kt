package org.cerion.symcalc

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.function.special.Gamma
import org.cerion.symcalc.function.special.Zeta
import org.junit.Test

private const val MIN_PRECISION = 100

class Special {

    @Test
    fun gammaLarge() {
        val x = RealBigDec("3.14", 500)
        Gamma(x).eval()
    }

    @Test
    fun gammaMultiple() {
        val x = RealBigDec("3.14", MIN_PRECISION)
        for(i in 0 until 30)
            Gamma(x).eval()
    }

    @Test
    fun zetaLarge() {
        val x = RealBigDec("3.14", 450)
        Zeta(x).eval()
    }

    @Test
    fun zetaMultiple() {
        val x = RealBigDec("3.14", 50)
        for(i in 0 until 1)
            if(Zeta(x).eval() is ErrorExpr)
                throw RuntimeException()
    }
}