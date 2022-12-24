package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.test.Test

internal class ArcCosTest {

    @Test
    fun exact() {
        ArcCos(-1) `==` Pi()
        ArcCos(Rational(-1,2)) `==` Rational(2, 3) * Pi()
        ArcCos(1) `==` 0
        ArcCos(Rational(1,2)) `==` Rational(1, 3) * Pi()
    }

    @Test
    fun evalDouble() {
        ArcCos(1.0) `==` 0.0
        ArcCos(0.0) `==` 1.5707963267948966
        ArcCos(-1.0) `==` 3.141592653589793
        ArcCos(5.0) `==` Double.NaN
    }

    @Test
    fun bigDecBasic() {
        ArcCos("-1.0000") `==` "3.1416"
        ArcCos("-0.75000") `==` "2.4189"
        ArcCos("-0.50000") `==` "2.0944"
        ArcCos("-0.25000") `==` "1.8235"
        ArcCos(RealBigDec("0.0000", 5)) `==` "1.5708" // TODO need better way to input 0 of certain precision
        ArcCos("0.25000") `==` "1.3181"
        ArcCos("0.50000") `==` "1.0472"
        ArcCos("0.75000") `==` "0.72273"
        ArcCos("1.0000") `==` "0.0000"
    }

    @Test
    fun evalBigDec() {
        ArcCos("0.9876543210123456789") `==` "0.1572969521806257177"
    }

    @Test
    fun complex() {
        ArcCos(Complex(1.0, 1.0)) `==` Complex(0.9045568943023813, -1.0612750619050355)
    }
}