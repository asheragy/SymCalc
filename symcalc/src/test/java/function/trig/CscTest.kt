package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Rational
import org.junit.Test

class CscTest {
    @Test
    fun exact() {
        Csc(Rational(1,6) * Pi()) `==` 2
        Csc(Rational(1,2) * Pi()) `==` 1
    }

    @Test
    fun double() {
        Csc(0.0) `==` ComplexInfinity()
        Csc(1.0) `==` 1.1883951057781212
        Csc(Math.PI / 2) `==` 1.0
    }

    @Test
    fun bigDec() {
        Csc("1.2345") `==` "1.0593"
    }
}