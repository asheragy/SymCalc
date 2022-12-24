package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.junit.Test

class SecTest {

    @Test
    fun exact() {
        Sec(Rational(-1,3) * Pi()) `==` 2
        Sec(Rational(-1,6) * Pi()) `==` Integer(2) * Power(3, Rational(-1,2))
        Sec(Rational(1,6) * Pi()) `==` Integer.TWO * Power(3, Rational(-1,2))
        Sec(Rational(1,3) * Pi()) `==` 2
    }

    @Test
    fun double() {
        Sec(0.0) `==` 1.0
        Sec(1.0) `==` 1.8508157176809255
        Sec(Math.PI) `==` -1.0
    }

    @Test
    fun bigDec() {
        Sec("1.2345") `==` "3.0304"

    }
}