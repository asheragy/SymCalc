package org.cerion.symcalc

import org.cerion.symcalc.number.RealBigDec
import org.junit.Test

private const val MIN_PRECISION = 100

class Arithmetic {

    @Test
    fun `log large close to 1`() {
        val x = RealBigDec("1.1", 4000)
        x.log()
    }

    @Test
    fun `log large close to 1 multiple`() {
        val x = RealBigDec("1.1", MIN_PRECISION)
        for(i in 0 until 8500)
            x.log()
    }

    @Test
    fun `log large`() {
        val x = RealBigDec("100.1", 3500)
        x.log()
    }

    @Test
    fun `log large with small value`() {
        val x = RealBigDec("0.00123", 900)
        x.log()
    }

    @Test
    fun power_multiple() {
        val x = RealBigDec("3.14", MIN_PRECISION)
        for(i in 0 until 2200)
            x.pow(x)
    }

    @Test
    fun power_large() {
        val x = RealBigDec("3.14", 3000)
        x.pow(x)
    }

    @Test
    fun sqrtLarge() {
        val x = RealBigDec("3.0", 140000)
        x.sqrt()
    }

    @Test
    fun sqrtMultiple() {
        val x = RealBigDec("3.0", MIN_PRECISION)
        for(i in 0 until 200000)
            x.sqrt()
    }

    @Test
    fun nthRoot() {
        val x = RealBigDec("11.0", MIN_PRECISION)
        repeat(70000) {
            x.root(7)
        }
    }

    @Test
    fun nthRootLarge() {
        val x = RealBigDec("11.0", 65000)
        x.root(7)
    }

    @Test
    fun exp() {
        val x = RealBigDec("11.0", MIN_PRECISION)
        repeat(12000) {
            x.exp()
        }
    }

    @Test
    fun expLarge() {
        val x = RealBigDec("11.0", 5000)
        x.exp()
    }
}