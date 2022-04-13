package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import org.junit.Test


internal class SinhTest {

    @Test
    fun exact() {
        assertAll(
                Sinh(Integer.ZERO) `==` Integer.ZERO,
                Sinh(Integer.TWO) `==` Sinh(Integer.TWO),
                Sinh(Integer.TWO.unaryMinus()) `==` Minus(Sinh(Integer.TWO)),
                Sinh(Log(Integer(2))) `==` Rational(3, 4),
                Sinh(Pi() * Complex.I) `==` Integer.ZERO,
                Sinh(Pi() * Complex(0, Rational.HALF)) `==` Complex.I,

                // TODO may need to add more cases to E^I*Pi*x
                //Sinh(Pi() * Complex(0, Rational(1,4))) `==` Times(Complex.I, Power(Integer.TWO, Rational(-1,2)))
                //Sinh(Pi() * Complex(0, Rational(1,5))) `==`
                // Sinh(Log(golden ratio)) == 1/2
        )
    }

    @Test
    fun bigDecimal() {
        assertAll(
                Sinh(RealBigDec("0.00")) `==` RealBigDec("0.0"),
                Sinh(RealBigDec("1.000000000")) `==` RealBigDec("1.175201194"),
                Sinh(RealBigDec("2.1555")) `==` RealBigDec("4.2582"),
                Sinh(RealBigDec("3.1415")) `==` RealBigDec("11.548"),
                Sinh(RealBigDec("5.4321")) `==` RealBigDec("114.31"),
                Sinh(RealBigDec("-5.4321")) `==` RealBigDec("-114.31"),
                Sinh(RealBigDec("9.999999999")) `==` RealBigDec("11013.23286"),
        )
    }
}