package org.cerion.symcalc

import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.log
import org.junit.Test

class Arithmetic {


    @Test
    fun `log large close to 1`() {
        val x = RealBigDec("1.1").forcePrecision(4000)
        x.log()
    }

    @Test
    fun `log large close to 1 multiple`() {
        val x = RealBigDec("1.1").forcePrecision(100)
        for(i in 0 until 8500)
            x.log()
    }

    // TODO add log2 computation so large values can be tested (currently works of pre-computed value of 1000 digits only)

    // TODO small values seems to not work either for larger precision


    @Test
    fun power_multiple() {
        val x = RealBigDec("3.14").increasePrecision(100)
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
        val x = RealBigDec("3.0").increasePrecision(130000)
        x.sqrt()
    }

    @Test
    fun sqrtMultiple() {
        val x = RealBigDec("3.0").increasePrecision(100)
        for(i in 0 until 200000)
            x.sqrt()
    }
}