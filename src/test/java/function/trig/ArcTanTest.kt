package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import kotlin.test.Test

class ArcTanTest {

    @Test
    fun exact() {
        assertAll(
                ArcTan(Integer.ZERO) `==` Integer.ZERO,
                ArcTan(Integer.ONE) `==` Divide(Pi(), Integer(4)),
                ArcTan(Divide(Integer.ONE, Sqrt(Integer(3)))) `==` Divide(Pi(), Integer(6)),
                ArcTan(Divide(Sqrt(Integer(3)), Integer(3))) `==` Divide(Pi(), Integer(6)),
                ArcTan(Sqrt(Integer(3))) `==` Divide(Pi(), Integer(3)),
                ArcTan(Infinity()) `==` Divide(Pi(), Integer.TWO),
                ArcTan(ComplexInfinity()) `==` Indeterminate()
        )
    }

    @Test
    fun bigDecimal() {
        assertAll(
                ArcTan(RealBigDec("0.0")) `==` RealBigDec("0.0"),
                ArcTan(RealBigDec("0.000001")) `==` RealBigDec("0.000001"),
                ArcTan(RealBigDec("0.1000000000")) `==` RealBigDec("0.09966865249"),
                ArcTan(RealBigDec("0.5000000000")) `==` RealBigDec("0.4636476090"),
                ArcTan(RealBigDec("1.234567890")) `==` RealBigDec("0.8899874934"),
                ArcTan(RealBigDec("5.0001")) `==` RealBigDec("1.3734"),
                ArcTan(RealBigDec("-5.0001")) `==` RealBigDec("-1.3734"),
                ArcTan(RealBigDec("3.14159265358979323846264338328")) `==` RealBigDec("1.26262725567891168344432208361"),
                ArcTan(RealBigDec("10000000.0000")) `==` RealBigDec("1.57079622679")
        )
    }

    // TODO_LP add complex
}