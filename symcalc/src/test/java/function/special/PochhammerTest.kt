package org.cerion.symcalc.function.special

import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.`==`
import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import org.junit.Test

internal class PochhammerTest {

    @Test
    fun integer() {
        assertAll(
                Pochhammer(Integer(0), Integer(0)) `==` Integer(1),
                Pochhammer(Integer(0), Integer(1)) `==` Integer(0),
                Pochhammer(Integer(1), Integer(0)) `==` Integer(1),
                Pochhammer(Integer(1), Integer(1)) `==` Integer(1),
                Pochhammer(Integer(2), Integer(1)) `==` Integer(2),
                Pochhammer(Integer(2), Integer(3)) `==` Integer(24),
                Pochhammer(Integer(3), Integer(3)) `==` Integer(60)
        )
    }

    @Test
    fun rational() {
        Pochhammer(Rational(5,2), Integer(5)).eval() `should equal` Rational(45045,32)
    }

    @Test
    fun realBigDec() {
        val x = RealBigDec("3.3333")
        assertAll(
                Pochhammer(x, Integer(0)) `==` Integer(1),
                Pochhammer(x, Integer(1)) `==` x,
                Pochhammer(x, Integer(2)) `==` RealBigDec("14.444"),
                Pochhammer(x, Integer(5)) `==` RealBigDec("3577.8"),
        )
    }
}