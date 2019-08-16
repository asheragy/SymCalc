package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Assert.assertEquals
import org.junit.Test

class PiTest {

    @Test
    fun basic() {
        assertEquals(Pi(), Pi().eval())
        assertEquals(RealDouble(3.141592653589793), N(Pi()).eval())

        assertEquals(RealBigDec("3.1416"), N(Pi(), Integer(4)).eval())
        assertEquals(RealBigDec("3.14159"), N(Pi(), Integer(5)).eval())

        assertEquals(RealBigDec("3.14159265358979323846"), N(Pi(), Integer(20)).eval())
        assertEquals(RealBigDec("3.14159265358979323846264338327950288419716939937511"), N(Pi(), Integer(50)).eval())
        assertEquals(RealBigDec("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170680"), N(Pi(), Integer(100)).eval())
    }
}