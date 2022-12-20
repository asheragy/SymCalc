package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.junit.Test

class HyperbolicTest {

    @Test
    fun zero() {
        Sinh(0) `==` 0
        Cosh(0) `==` 1
        Tanh(0) `==` 0
        Sech(0) `==` 1
        Csch(0) `==` ComplexInfinity()
        Coth(0) `==` ComplexInfinity()
        // TODO others
    }

    @Test
    fun infinity() {
        val x = Infinity()
        Sinh(x) `==` Infinity()
        Cosh(x) `==` Infinity()
        Tanh(x) `==` 1
        Sech(x) `==` 0
        Csch(x) `==` 0
        Coth(x) `==` 1

        // TODO others
    }

    @Test
    fun complexInfinity() {
        val x = ComplexInfinity()
        Sinh(x) `==` Indeterminate()
        Cosh(x) `==` Indeterminate()
        Tanh(x) `==` Indeterminate()
        Sech(x) `==` Indeterminate()
        Csch(x) `==` Indeterminate()
        Coth(x) `==` Indeterminate()
        // TODO others
    }
}