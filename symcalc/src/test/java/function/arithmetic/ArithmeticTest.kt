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
        Log(0) `==` Infinity(-1)
    }

    @Test
    fun infinity() {
        val x = Infinity()

        Minus(x) `==` Infinity(-1)

        Plus(x, x) `==` Infinity()
        Plus(x, 5) `==` Infinity()
        Plus(5, x) `==` Infinity()

        Subtract(x, x) `==` Indeterminate()
        Subtract(x, 5) `==` Infinity()
        Subtract(5, x) `==` Infinity(-1)
        Subtract(Log(2), x) `==` Infinity(-1)
        Subtract(x, Log(2)) `==` Infinity()

        Log(x) `==` Infinity()
        // TODO add others
    }

    @Test
    fun negativeInfinity() {
        val x = Infinity(-1)

        Minus(x) `==`  Infinity()

        Subtract(x, 1) `==` Infinity(-1)

        Times(x, -1) `==` Infinity()
        Times(-1, x) `==` Infinity()
        //Times(x, x) `==` Infinity()
        Times(x, 1) `==` Infinity(-1)
    }

    @Test
    fun complexInfinity() {
        val x = ComplexInfinity()

        Power(x, 2) `==` ComplexInfinity()
        Power(x, -2) `==` 0

        Log(x) `==` Infinity()

        Exp(x) `==` Indeterminate()
    }
}