package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Complex
import org.junit.Test

class ArcReciprocalTest {

    @Test
    fun arcSech_exact() {
        // TODO Log not able to evaluate this yet, same with others below
        //ArcSech(-2) `==` Pi() * Complex(0, Rational(2, 3))
        ArcSech(-1) `==` Pi() * Complex(0, 1)
        ArcSech(1) `==` 0
        //ArcSech(2) `==` Pi() * Complex(0, Rational(1, 3))
    }

    @Test
    fun arcSech() {
        ArcSech("-3.1415") `==` Complex(0, "1.8948")
        ArcSech("-0.33333") `==` Complex("1.7628", "3.1416")
        ArcSech("0.33333") `==` "1.7628"
        ArcSech("3.1415") `==` Complex(0, "1.2468")
    }

    @Test
    fun arcCsch_exact() {
        //ArcCsch(Complex(0, -2)) `==` Pi() * Complex(0, Rational(1, 6))
        //ArcCsch(Complex(0, -1)) `==` Pi() * Complex(0, Rational(1, 2))
        //ArcCsch(Complex(0, 1)) `==` Pi() * Complex(0, Rational(-1, 2))
        //ArcCsch(Complex(0, 2)) `==` Pi() * Complex(0, Rational(-1, 6))
    }

    @Test
    fun arcCsch() {
        ArcCsch("-3.1415") `==` "-0.31317"
        ArcCsch("-0.33333") `==` "-1.8185"
        ArcCsch("0.33333") `==` "1.8185"
        ArcCsch("3.1415") `==` "0.31317"
    }

    @Test
    fun arcCoth_exact() {
        //ArcCoth(1) `==` Infinity()
        //ArcCoth(Complex(0, -1)) `==` Pi() * Complex(0, Rational(1, 4))
        //ArcCoth(Complex(0, 1)) `==` Pi() * Complex(0, Rational(-1, 4))
    }

    @Test
    fun arcCoth() {
        ArcCoth("-3.1415") `==` "-0.32978"
        ArcCoth("-0.33333") `==` Complex("-0.34657", "-1.5708")
        ArcCoth("0.33333") `==` Complex("0.34657", "-1.5708")
        ArcCoth("3.1415") `==` "0.32978"
    }
}