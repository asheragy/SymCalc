package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.junit.Test

class ArithmeticTest {

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
        Sqrt(x) `==` Infinity()
        Power(x, 2) `==` Infinity()
        Power(x, 3) `==` Infinity()
        Power(5, x) `==` Infinity()
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

        Log(x) `==` Infinity()
        Sqrt(x) `==` I() * Infinity() // TODO should actually be Infinity(Complex(0, 1))

        Power(x, 2) `==` Infinity(1)
        Power(x, 3) `==` Infinity(-1)
        Power(5, x) `==` 0
    }

    @Test
    fun complexInfinity() {
        val x = ComplexInfinity()

        Plus(5, x) `==` ComplexInfinity()
        Subtract(5, x) `==` ComplexInfinity()
        Times(5, x) `==` ComplexInfinity()
        Divide(x, 5) `==` ComplexInfinity()

        Power(x, 2) `==` ComplexInfinity()
        Power(x, -2) `==` 0

        Log(x) `==` Infinity()

        Exp(x) `==` Indeterminate()

    }
}