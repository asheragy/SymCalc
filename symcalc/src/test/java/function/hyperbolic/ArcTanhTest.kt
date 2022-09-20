package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import org.junit.Test
import org.junit.jupiter.api.Assertions

internal class ArcTanhTest {

    @Test
    fun exact() {
        Assertions.assertAll(
            ArcTanh(Integer(-1)) `==` Minus(Infinity()),
            ArcTanh(Integer(0)) `==` Integer(0),
            ArcTanh(Integer(1)) `==` Infinity(),
            ArcTanh(Infinity()) `==` Times(Complex(0, Rational.HALF.unaryMinus()), Pi())
        )
    }

    @Test
    fun bigDecimal() {
        Assertions.assertAll(
            ArcTanh(RealBigDec("0.5", 50)) `==` RealBigDec("0.54930614433405484569762261846126285232374527891137"),
        )
    }
}