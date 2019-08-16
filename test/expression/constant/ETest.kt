package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Assert.*
import org.junit.Test

class ETest {
    @Test
    fun basic() {
        assertEquals(E(), E().eval())
        assertEquals(RealDouble(2.718281828459045), N(E()).eval())

        assertEquals(RealBigDec("2.7183"), N(E(), Integer(4)).eval())
        assertEquals(RealBigDec("2.71828"), N(E(), Integer(5)).eval())

        assertEquals(RealBigDec("2.71828182845904523536"), N(E(), Integer(20)).eval())
        assertEquals(RealBigDec("2.71828182845904523536028747135266249775724709369996"), N(E(), Integer(50)).eval())
        assertEquals(RealBigDec("2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274"), N(E(), Integer(100)).eval())
    }
}