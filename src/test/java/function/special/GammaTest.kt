package org.cerion.symcalc.function.special

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll

internal class GammaTest {

    @Test
    fun integers() {
        assertAll(
                Gamma(-100) `==` ComplexInfinity(),
                Gamma(-99) `==` ComplexInfinity(),
                Gamma(-2) `==` ComplexInfinity(),
                Gamma(-1) `==` ComplexInfinity(),
                Gamma(0) `==` ComplexInfinity(),
                Gamma(1) `==` 1,
                Gamma(2) `==` 1,
                Gamma(10) `==` 362880)
    }

    @Test
    fun rational() {
        assertAll(
                Gamma(Rational(-21, 2)) `==` Times(Rational(-2048, "13749310575"), Power(Pi(), Rational.HALF)),
                Gamma(Rational(-3, 2)) `==` Times(Rational(4, 3), Power(Pi(), Rational.HALF)),
                Gamma(Rational(-1, 2)) `==` Times(-2, Power(Pi(), Rational.HALF)),
                Gamma(Rational(1, 2)) `==` Power(Pi(), Rational.HALF),
                Gamma(Rational(3, 2)) `==` Times(Rational.HALF, Power(Pi(), Rational.HALF)),
                Gamma(Rational(21, 2)) `==` Times(Rational(654729075, 1024), Power(Pi(), Rational.HALF)),
                // Not computable
                Gamma(Rational(1, 3)) `==` Gamma(Rational(1, 3)),
                Gamma(Rational(-3, 5)) `==` Gamma(Rational(-3, 5)))
    }

    @Test
    fun realBigDec() {
        assertAll(
                Gamma(RealBigDec("0.1111111111")) `==` "8.522688140",
                Gamma(RealBigDec("1.222222222")) `==` "0.9125732371",
                Gamma(RealBigDec("2.222222222")) `==` "1.115367290",
                Gamma(RealBigDec("5.555555555")) `==` "57.26133666",
                Gamma(RealBigDec("6.54321")) `==` "311.123",
                Gamma(RealBigDec("-3.222222222")) `==` "0.6119578328"
        )
    }

    @Test
    fun realBigDec_larger() {
        assertAll(
                Gamma(Pi().eval(50)) `==` "2.2880377953400324179595889090602339228896881533562"
        )
    }

    // TODO should be slightly faster + working on high precision

    //@Test
    fun highPrecision() {
        assertAll(
                Gamma(Pi().eval(100)) `==` "2.288037795340032417959588909060233922889688153356222441199380745470471006608504282500725304467928475",
                Gamma(Power(Pi(),Integer(2)).eval(100)) `==` "270792.2054495102160290246741198906841378682168817929885534109780010824127805549219880866939727858013",
                Gamma(Divide(Integer(1), Integer(1000)).eval(100)) `==` "999.4237724845954661149822012996440004652176101456122324695421716913960238118284038452483877721898402"
        )
    }
}