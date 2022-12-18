package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import kotlin.test.Test

class ArcTanTest {

    @Test
    fun exact() {
        ArcTan(0) `==` Integer.ZERO
        ArcTan(1) `==` Rational(1,4) * Pi()
        ArcTan(-1) `==` Rational(-1,4) * Pi()
        ArcTan(Divide(1, Sqrt(3))) `==` Divide(Pi(), 6)
        ArcTan(Divide(Sqrt(3), 3)) `==` Divide(Pi(), 6)
        ArcTan(Sqrt(3)) `==` Divide(Pi(), 3)
        ArcTan(Infinity()) `==` Divide(Pi(), 2)
    }

    @Test
    fun bigDecimal() {
        ArcTan("0.0") `==` "0.0"
        ArcTan("0.000001") `==` "0.000001"
        ArcTan("0.1000000000") `==` "0.09966865249"
        ArcTan("0.5000000000") `==` "0.4636476090"
        ArcTan("1.234567890") `==` "0.8899874934"
        ArcTan("5.0001") `==` "1.3734"
        ArcTan("-5.0001") `==` "-1.3734"
        ArcTan("3.14159265358979323846264338328") `==` "1.26262725567891168344432208361"
        ArcTan("10000000.0000") `==` "1.57079622679"
    }

    @Test
    fun double() {
        ArcTan(-2.0) `==` -1.1071487177940904
        ArcTan(0.0) `==` 0.0
        ArcTan(1.0) `==` 0.7853981633974483
    }

    @Test
    fun atan2() {
        // 4 quadrants
        ArcTan(1.0, 1.0) `==` 0.7853981633974483
        ArcTan(1.0, -2.0) `==` -1.1071487177940904
        ArcTan(-2.0, 3.0) `==` 2.158798930342464
        ArcTan(-3.0, -4.0) `==` -2.214297435588181

        ArcTan(0, 0) `==` Indeterminate()
        ArcTan(1.0, 0) `==` 0.0
        ArcTan(0, 1.0) `==` Pi() / 2
        ArcTan(0, -1.0) `==` Rational.HALF.unaryMinus() * Pi()
    }

    // TODO add complex
}