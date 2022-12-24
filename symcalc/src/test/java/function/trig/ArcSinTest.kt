package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Rational
import kotlin.test.Test

internal class ArcSinTest {

    @Test
    fun asInteger() {
        ArcSin(1) `==` Rational(1,2) * Pi()
        ArcSin(-1) `==` Rational(-1,2) * Pi()
    }

    @Test
    fun asDouble() {
        ArcSin(1.0) `==` 1.5707963267948966
        ArcSin(0.0) `==` 0.0
        ArcSin(-1.0) `==` -1.5707963267948966
        ArcSin(5.0) `==` Double.NaN
    }

    @Test
    fun bigDecBasic() {
        ArcSin("-1.0000") `==` "-1.5708"
        ArcSin("-0.75000") `==` "-0.84806"
        ArcSin("-0.50000") `==` "-0.52360"
        ArcSin("-0.25000") `==` "-0.25268"
        ArcSin("0.0000") `==` "0.0000"
        ArcSin("0.25000") `==` "0.25268"
        ArcSin("0.50000") `==` "0.52360"
        ArcSin("0.75000") `==` "0.84806"
        ArcSin("1.0000") `==` "1.5708"
    }

    @Test
    fun bigDecLarge() {
        ArcSin("0.9876543210123456789") `==` "1.413499374614270902"
    }

    @Test
    fun complex() {
        ArcSin(Complex(1.0, 1.0)) `==` Complex(0.6662394324925153, 1.0612750619050355)
    }
}