package org.cerion.symcalc

import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.number.RealBigDec
import org.junit.Test

private const val MIN_PRECISION = 100

class Arithmetic {

    @Test
    fun `log large close to 1`() {
        val x = RealBigDec("1.1").increasePrecision(4000)
        x.log()
    }

    @Test
    fun `log large close to 1 multiple`() {
        val x = RealBigDec("1.1").increasePrecision(MIN_PRECISION)
        for(i in 0 until 8500)
            x.log()
    }

    // TODO add log2 computation so large values can be tested (currently works of pre-computed value of 1000 digits only)

    // TODO small values seems to not work either for larger precision


    @Test
    fun power_multiple() {
        val x = RealBigDec("3.14").increasePrecision(MIN_PRECISION)
        for(i in 0 until 2000)
            x.pow(x)
    }

    /* Same issue log has
    @Test
    fun power_large() {
        val bd = RealBigDec("3.14").forcePrecision(900)
        val x = RealBigDec(bd)
        x.pow(x)
    }

     */

    @Test
    fun sqrtLarge() {
        val x = RealBigDec("3.0").increasePrecision(140000)
        x.sqrt()
    }

    @Test
    fun sqrtMultiple() {
        val x = RealBigDec("3.0").increasePrecision(MIN_PRECISION)
        for(i in 0 until 200000)
            x.sqrt()
    }

    @Test
    fun nthRoot() {
        val x = RealBigDec("11.0").increasePrecision(MIN_PRECISION)
        repeat(70000) {
            x.root(7)
        }
    }

    @Test
    fun nthRootLarge() {
        val x = RealBigDec("11.0").increasePrecision(65000)
        x.root(7)
    }

    @Test
    fun exp() {
        val x = RealBigDec("11.0").increasePrecision(MIN_PRECISION)
        repeat(4500) {
            x.exp()
        }
    }

    @Test
    fun expLarge() {
        val x = RealBigDec("11.0").increasePrecision(3500)
        x.exp()
    }
}