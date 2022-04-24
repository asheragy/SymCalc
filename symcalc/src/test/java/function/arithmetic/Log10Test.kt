package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import org.cerion.symcalc.`should equal`
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class Log10Test {

    @Test
    fun basic() {
        assertEquals(RealDouble(2.0), Log10(RealDouble(100.0)).eval())
        assertEquals(RealDouble(1.6989700043360187), Log10(RealDouble(50.0)).eval())
    }

    @Test
    fun bigDec() {
        Log10(RealBigDec("20", 50)).eval() `should equal` "1.3010299956639811952137388947244930267681898814621"
    }
}