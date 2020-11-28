package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import kotlin.test.Test
import kotlin.test.assertEquals

class ArcTanTest {

    @Test
    fun basic() {
        assertEquals(Divide(Pi(), Integer(4)), ArcTan(Integer.ONE).eval())
    }

    @Test
    fun bigDecimal() {
        assertEquals(RealBigDec("1.3734"), ArcTan(RealBigDec("5.0001")).eval())
        assertEquals(RealBigDec("1.26262725567891168344432208361"), ArcTan(RealBigDec("3.14159265358979323846264338328")).eval())
    }
}