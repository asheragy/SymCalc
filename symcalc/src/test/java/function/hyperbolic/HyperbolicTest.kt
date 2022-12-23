package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Rational
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

        ArcSinh(0) `==` 0
        ArcCosh(0) `==` Pi() * Complex(0, Rational(1, 2))
        ArcTanh(0) `==` 0
        ArcCsch(0) `==` ComplexInfinity()
        ArcSech(0) `==` Infinity()
        ArcCoth(0) `==` Pi() * Complex(0, Rational(1, 2))
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

        ArcSinh(x) `==` Infinity()
        ArcCosh(x) `==` Infinity()
        ArcTanh(x) `==` Pi() * Complex(0, Rational(-1, 2))
        ArcCsch(x) `==` 0
        ArcSech(x) `==` Pi() * Complex(0, Rational(1, 2))
        ArcCoth(x) `==` 0
    }

    // TODO negative infinity

    @Test
    fun complexInfinity() {
        val x = ComplexInfinity()
        Sinh(x) `==` Indeterminate()
        Cosh(x) `==` Indeterminate()
        Tanh(x) `==` Indeterminate()
        Sech(x) `==` Indeterminate()
        Csch(x) `==` Indeterminate()
        Coth(x) `==` Indeterminate()

        ArcSinh(x) `==` ComplexInfinity()
        ArcCosh(x) `==` Infinity()
        ArcTanh(x) `==` Indeterminate()
        ArcCsch(x) `==` 0
        ArcSech(x) `==` Indeterminate()
        ArcCoth(x) `==` 0
    }
}