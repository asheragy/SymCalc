package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.junit.Test

class ArithmeticTest {

    // TODO reflection to see if anything got added here

    @Test
    fun zero() {
        Log(0) `==` Minus(Infinity())
    }

    @Test
    fun infinity() {
        val x = Infinity()

        Plus(x, x) `==` Infinity()
        Plus(x, 5) `==` Infinity()
        Plus(5, x) `==` Infinity()

        Subtract(x, x) `==` Indeterminate()
        Subtract(x, 5) `==` Infinity()
        Subtract(5, x) `==` Minus(Infinity())

        Log(x) `==` Infinity()
        // TODO add others
    }

    @Test
    fun negativeInfinity() {
        val x = Minus(Infinity())

        Subtract(x, 1) `==` Minus(Infinity())

        Times(x, -1) `==` Infinity()
        Times(-1, x) `==` Infinity()
        //Times(x, x) `==` Infinity()
        Times(x, 1) `==` Minus(Infinity())
    }

    @Test
    fun complexInfinity() {
        val x = ComplexInfinity()

        Log(x) `==` Infinity()
    }
}