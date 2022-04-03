package org.cerion.math.bignum.extensions

import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class BigDecConst {

    @Test
    fun e_Stored() {
        assertEquals(BigDecimal("3"), getEToPrecision(1))
        assertEquals(BigDecimal("2.7"), getEToPrecision(2))
        assertEquals(BigDecimal("2.72"), getEToPrecision(3))
        assertEquals(BigDecimal("2.718"), getEToPrecision(4))
    }

    @Test
    fun e_Computed() {
        assertEquals(BigDecimal("3"), calculateE(1))
        assertEquals(BigDecimal("2.7"), calculateE(2))
        assertEquals(BigDecimal("2.72"), calculateE(3))
        assertEquals(BigDecimal("2.718"), calculateE(4))
    }
}