package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Rational
import org.junit.Test

val pi = Pi()
class ReciprocalTest {

    @Test
    fun sech() {
        Sech(1.7) `==` 0.3535673495014021
        Sech(pi * Complex(0, Rational(-1,3))) `==` 2
        Sech(pi * Complex(0, Rational(1,6))) `==` Times(2, Power(3, Rational(-1, 2)))
        Sech(pi * Complex(0, Rational(1,3))) `==` 2
    }

    @Test
    fun csch() {
        Csch(1.7) `==` 0.37798152766836207
        Csch(pi * Complex(0, Rational(-1,6))) `==` Complex(0, 2)
        Csch(pi * Complex(0, Rational(1,6))) `==` Complex(0, -2)
        Csch(pi * Complex(0, Rational(1,3))) `==` Times(Complex(0, -2), Power(3, Rational(-1,2)))
        Csch(pi * Complex(0, Rational(1,2))) `==` Complex(0, -1)
    }

    @Test
    fun coth() {
        Coth(1.7) `==` 1.0690509975012925
        Coth(pi * Complex(0, Rational(1,2))) `==` 0
        Coth(pi * Complex(0, Rational(1,3))) `==` Times(Complex(0, -1), Power(3, Rational(-1, 2)))
        Coth(pi * Complex(0, Rational(1,4))) `==` Complex(0, -1)
        Coth(pi * Complex(0, Rational(1,6))) `==` Times(Complex(0, -1), Power(3, Rational(1, 2)))
    }
}