package org.cerion.math.bignum

import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.math.BigDecimal
import kotlin.test.*

@ExperimentalUnsignedTypes
class BigDecTest {

    @Test
    fun fromString() {
        BigDec("10000000000.0").apply {
            assertEquals(1, scale)
            assertEquals("100000000000", value.toString())
        }

        BigDec("10000000000").apply {
            assertEquals(0, scale)
            assertEquals("10000000000", value.toString())
        }

        BigDec("0.000001").apply {
            assertEquals(6, scale)
            assertEquals("1", value.toString())
        }
    }
}