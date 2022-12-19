package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Rational
import org.junit.Test

internal class TanhTest {

    @Test
    fun exact() {
        Tanh(2) `==` Tanh(2)
        Tanh(Log(2)) `==` Rational(3, 5)
        Tanh(Pi() * Complex(0, Rational.HALF)) `==` ComplexInfinity()
        Tanh(Pi() * Complex(0, Rational(1,4))) `==` Complex.I
        Tanh(Pi() * Complex(0, Rational(1,3))) `==` Complex.I * Power(3, Rational.HALF)
    }

    @Test
    fun bigDecimal() {
        Tanh("0.00") `==` "0.0"
        Tanh("1.000000000") `==` "0.7615941560"
        Tanh("2.1555") `==` "0.97352"
        Tanh("3.1415") `==` "0.99627"
        Tanh("3.1415926535897932384626433832795028841971693993751") `==` "0.99627207622074994426469058001253671189689919080459"
        Tanh("5.4321") `==` "0.99996"
        Tanh("-5.4321") `==` "-0.99996"
        Tanh("9.999999999") `==` "0.9999999959"
    }
}