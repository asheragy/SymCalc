package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.cerion.symcalc.expression.number.RealNum_BigDecimal
import org.cerion.symcalc.expression.number.RealNum_Double
import org.junit.Assert.*
import org.junit.Test

class ETest {
    @Test
    fun basic() {
        assertEquals(E(), E().eval())
        assertEquals(RealNum_Double(2.718281828459045), N(E()).eval())

        assertEquals(RealNum_BigDecimal("2.7183"), N(E(), IntegerNum(4)).eval())
        assertEquals(RealNum_BigDecimal("2.71828"), N(E(), IntegerNum(5)).eval())

        assertEquals(RealNum_BigDecimal("2.71828182845904523536"), N(E(), IntegerNum(20)).eval())
        assertEquals(RealNum_BigDecimal("2.71828182845904523536028747135266249775724709369996"), N(E(), IntegerNum(50)).eval())
        assertEquals(RealNum_BigDecimal("2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274"), N(E(), IntegerNum(100)).eval())
    }
}