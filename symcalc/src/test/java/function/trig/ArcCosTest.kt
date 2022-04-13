package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.test.Test

internal class ArcCosTest {

    @Test
    fun asInteger() {
        ArcCos(1).eval() `should equal` 0
        ArcCos(0).eval() `should equal` Rational(1,2) * Pi()
        ArcCos(-1).eval() `should equal` Pi()
    }

    @Test
    fun evalDouble() {
        ArcCos(1.0).eval() `should equal` 0.0
        ArcCos(0.0).eval() `should equal` 1.5707963267948966
        ArcCos(-1.0).eval() `should equal` 3.141592653589793
        ArcCos(5.0).eval() `should equal` Double.NaN
    }

    @Test
    fun bigDecBasic() {
        ArcCos("-1.0000").eval() `should equal` "3.1416"
        ArcCos("-0.75000").eval() `should equal` "2.4189"
        ArcCos("-0.50000").eval() `should equal` "2.0944"
        ArcCos("-0.25000").eval() `should equal` "1.8235"
        ArcCos(RealBigDec("0.0000", 5)).eval() `should equal` "1.5708" // TODO_LP need better way to input 0 of certain precision
        ArcCos("0.25000").eval() `should equal` "1.3181"
        ArcCos("0.50000").eval() `should equal` "1.0472"
        ArcCos("0.75000").eval() `should equal` "0.72273"
        ArcCos("1.0000").eval() `should equal` "0.0000"
    }

    @Test
    fun evalBigDec() {
        ArcCos("0.9876543210123456789").eval() `should equal` "0.1572969521806257177"
    }
}