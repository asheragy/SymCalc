package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.number.RealDouble
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class Log10Test {

    @Test
    fun basic() {
        assertEquals(RealDouble(2.0), Log10(RealDouble(100.0)).eval())
        assertEquals(RealDouble(1.6989700043360187), Log10(RealDouble(50.0)).eval())
    }
}