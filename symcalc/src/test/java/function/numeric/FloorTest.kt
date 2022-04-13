package org.cerion.symcalc.function.numeric

import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealDouble
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

internal class FloorTest {

    @Test
    fun basic() {
        // NumberExpr handled by classes so not really anything to do here
        assertEquals(Integer(5), Floor(RealDouble(5.00000000001)).eval())
    }
}