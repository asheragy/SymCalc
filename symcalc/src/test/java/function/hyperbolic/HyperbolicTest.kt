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
        // TODO others
    }

    @Test
    fun infinity() {
        Cosh(Infinity()) `==` Infinity()
        Tanh(Infinity()) `==` 1
        // TODO others
    }

    @Test
    fun complexInfinity() {
        Cosh(ComplexInfinity()) `==` Indeterminate()
        Tanh(ComplexInfinity()) `==` Indeterminate()
        // TODO others
    }
}