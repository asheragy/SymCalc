package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.junit.Test


internal class SinhTest {

    @Test
    fun exact() {
        Sinh(2) `==` Sinh(2)
        Sinh(-2) `==` Minus(Sinh(2))
        Sinh(Log(Integer(2))) `==` Rational(3, 4)
        Sinh(Pi() * Complex.I) `==` 0
        Sinh(Pi() * Complex(0, Rational.HALF)) `==` Complex.I

        Sinh(Pi() * Complex(0, Rational(1,6))) `==` Complex(0, Rational.HALF)
        Sinh(Pi() * Complex(0, Rational(1,3))) `==` Complex(0, Rational.HALF) * Power(3, Rational.HALF)

        Sinh(Pi() * Complex(0, Rational(1,4))) `==` Times(Complex.I, Power(Integer.TWO, Rational(-1,2)))
        // unusual result but can work
        //Sinh(Pi() * Complex(0, Rational(1,5))) `==` 1
    }

    @Test
    fun bigDecimal() {
        Sinh("0.00") `==` "0.0"
        Sinh("1.000000000") `==` "1.175201194"
        Sinh("2.1555") `==` "4.2582"
        Sinh("3.1415") `==` "11.548"
        Sinh("5.4321") `==` "114.31"
        Sinh("-5.4321") `==` "-114.31"
        Sinh("9.999999999") `==` "11013.23286"
    }
}