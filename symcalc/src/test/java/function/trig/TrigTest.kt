package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.junit.Test

class TrigTest {

    @Test
    fun zero() {
        val x = Integer(0)
        Sin(x) `==` 0
        Cos(x) `==` 1
        Tan(x) `==` 0
        Sec(x) `==` 1
        Csc(x) `==` ComplexInfinity()
        Cot(x) `==` ComplexInfinity()

        ArcSin(x) `==` 0
        ArcCos(x) `==` Rational(1,2) * Pi()
        ArcTan(x) `==` 0
        ArcSec(x) `==` ComplexInfinity()
        ArcCsc(x) `==` ComplexInfinity()
        ArcCot(x) `==` Rational(1,2) * Pi()
    }

    @Test
    fun pi() {
        val x = Pi()

        Sin(x) `==` 0
        Cos(x) `==` -1
        Tan(x) `==` 0
        Sec(x) `==` -1
        Csc(x) `==` ComplexInfinity()
        Cot(x) `==` ComplexInfinity()
    }

    @Test
    fun one() {
        val x = Integer(1)
        ArcSin(x) `==` Pi() * Rational(1, 2)
        ArcCos(x) `==` 0
        ArcTan(x) `==` Pi() * Rational(1, 4)
        ArcSec(x) `==` 0
        ArcCsc(x) `==` Pi() * Rational(1, 2)
        ArcCot(x) `==` Pi() * Rational(1, 4)

        val y = Integer(-1)
        ArcSin(y) `==` Pi() * Rational(-1, 2)
        ArcCos(y) `==` Pi()
        ArcTan(y) `==` Pi() * Rational(-1, 4)
        ArcSec(y) `==` Pi()
        ArcCsc(y) `==` Pi() * Rational(-1, 2)
        ArcCot(y) `==` Pi() * Rational(-1, 4)
    }

    @Test
    fun infinity() {
        val x = Infinity()

        ArcTan(x) `==` Divide(Pi(), 2)
    }

    @Test
    fun complexInfinity() {
        val ci = ComplexInfinity()
        Sin(ci) `==` Indeterminate()
        Cos(ci) `==` Indeterminate()
        Tan(ci) `==` Indeterminate()
        Sec(ci) `==` Indeterminate()
        Csc(ci) `==` Indeterminate()
        Cot(ci) `==` Indeterminate()

        ArcSin(ci) `==` ComplexInfinity()
        ArcCos(ci) `==` ComplexInfinity()
        ArcTan(ci) `==` Indeterminate()
        ArcSec(ci) `==` Pi() / Integer.TWO
        ArcCsc(ci) `==` 0
        ArcCot(ci) `==` 0
    }
}