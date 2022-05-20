package decimal

import org.cerion.math.bignum.decimal.calculateE
import org.cerion.math.bignum.decimal.getEToPrecision
import org.cerion.math.bignum.decimal.getPiToDigits
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class ConstTest {

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

    @Test
    fun pi() {
        val pi100 = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679"

        for(i in 2 until 20) {
            val computed = getPiToDigits(i).toString()
            assertEquals(pi100.substring(0, computed.length-2), computed.substring(0, computed.length - 2))
        }
    }
}