package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.cerion.symcalc.expression.number.RealNum_BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Test

class PiTest {

    @Test
    fun basic() {
        assertEquals(Pi(), Pi().eval())
        assertEquals(RealNum.create(3.141592653589793), N(Pi()).eval())

        assertEquals(RealNum_BigDecimal("3.1416"), N(Pi(), IntegerNum(4)).eval())
        assertEquals(RealNum_BigDecimal("3.14159"), N(Pi(), IntegerNum(5)).eval())

        assertEquals(RealNum.create("3.14159265358979323846"), N(Pi(), IntegerNum(20)).eval())
        assertEquals(RealNum.create("3.14159265358979323846264338327950288419716939937511"), N(Pi(), IntegerNum(50)).eval())
        assertEquals(RealNum.create("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170680"), N(Pi(), IntegerNum(100)).eval())
    }
}