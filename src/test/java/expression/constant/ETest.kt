package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ETest {
    @Test
    fun basic() {
        assertEquals(E(), E().eval())
        assertEquals(RealDouble(2.718281828459045), N(E()).eval())

        assertEquals(RealBigDec("2.718"), N(E(), Integer(4)).eval())
        assertEquals(RealBigDec("2.7183"), N(E(), Integer(5)).eval())

        assertEquals(RealBigDec("2.7182818284590452354"), N(E(), Integer(20)).eval())
        assertEquals(RealBigDec("2.7182818284590452353602874713526624977572470937000"), N(E(), Integer(50)).eval())
        assertEquals(RealBigDec("2.718281828459045235360287471352662497757247093699959574966967627724076630353547594571382178525166427"), N(E(), Integer(100)).eval())
    }
}