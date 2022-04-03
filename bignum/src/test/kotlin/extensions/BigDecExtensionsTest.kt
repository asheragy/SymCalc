package org.cerion.math.bignum.extensions

import org.cerion.math.bignum.sqrt
import org.junit.Test
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.test.assertEquals

class BigDecExtensionsTest {

    @Test
    fun sqrt() {
        val mc = MathContext(10 ,RoundingMode.HALF_UP)
        assertEquals(BigDecimal(0), BigDecimal(0).sqrt(mc))
        assertEquals(BigDecimal("0.00000"), BigDecimal("0.0000000000").sqrt(mc))

        // TODO zero does not work currently
        //assertEquals(BigDecimal("0"), BigDecimal(0).sqrt(10))
        assertEquals(BigDecimal("2.236067978"), BigDecimal(5).sqrt(10))
    }
}