package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.constant.Indeterminate
import org.cerion.symcalc.expression.constant.Infinity
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class TanhTest {

    @Test
    fun exact() {
        org.cerion.symcalc.assertAll(
                Tanh(Integer.ZERO) `==` Integer.ZERO,
                Tanh(Integer.TWO) `==` Tanh(Integer.TWO),
                Tanh(Infinity()) `==` Integer.ONE,
                Tanh(ComplexInfinity()) `==` Indeterminate(),
                Tanh(Log(Integer(2))) `==` Rational(3, 5),
                Tanh(Pi() * Complex(0, Rational.HALF)) `==` ComplexInfinity(),
                Tanh(Pi() * Complex(0, Rational(1,4))) `==` Complex.I,
                //Tanh(Pi() * Complex(0, Rational(1,3))) `==` I * sqrt(3)
                //Tanh(Log(golden ratio)) == 1/5 * Sqrt(5)
        )
    }

    @Test
    fun bigDecimal() {
        assertAll(
                Tanh(RealBigDec("0.00")) `==` RealBigDec("0.0"),
                Tanh(RealBigDec("1.000000000")) `==` RealBigDec("0.7615941560"),
                Tanh(RealBigDec("2.1555")) `==` RealBigDec("0.97352"),
                Tanh(RealBigDec("3.1415")) `==` RealBigDec("0.99627"),
                Tanh(RealBigDec("3.1415926535897932384626433832795028841971693993751")) `==` RealBigDec("0.99627207622074994426469058001253671189689919080459"),
                Tanh(RealBigDec("5.4321")) `==` RealBigDec("0.99996"),
                Tanh(RealBigDec("-5.4321")) `==` RealBigDec("-0.99996"),
                Tanh(RealBigDec("9.999999999")) `==` RealBigDec("0.9999999959"),
        )
    }
}