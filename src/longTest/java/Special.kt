package org.cerion.symcalc

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.function.special.Gamma
import org.cerion.symcalc.function.special.Zeta
import org.cerion.symcalc.number.Integer
import org.junit.Test

private const val MIN_PRECISION = 100

class Special {

    @Test
    fun gammaLarge() {
        val x = RealBigDec("3.14", 500)
        run(Gamma(x))
    }

    @Test
    fun gammaMultiple() {
        val x = RealBigDec("3.14", MIN_PRECISION)
        run(30, Gamma(x))
    }

    @Test
    fun zetaLarge() {
        val x = RealBigDec("3.14", 450)
        run(Zeta(x))
    }

    @Test
    fun zetaMultiple() {
        val x = RealBigDec("3.14", 50)
        for(i in 0 until 75)
            if(Zeta(x).eval() is ErrorExpr) // TODO add wrapper for everything that checks result
                throw RuntimeException()
    }

    @Test
    fun zetaOddInteger_multiple() {
        repeat(4) {
            for (i in 3 until 100) {
                run(Zeta(Integer(i).toPrecision(100)))
            }
        }
    }

    @Test
    fun zetaOddInteger_Large() {
        for (i in 3 until 100) {
            run(Zeta(Integer(i).toPrecision(300)))
        }
    }
}