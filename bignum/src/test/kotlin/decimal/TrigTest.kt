package decimal

import org.cerion.math.bignum.decimal.cos
import org.cerion.math.bignum.decimal.sin
import org.cerion.math.bignum.decimal.tan
import org.cerion.math.bignum.decimal.tanh
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class TrigTest {

    @Test
    fun sin() {
        assertEquals(BigDecimal("0.0009999998333"), BigDecimal(0.001).sin(10))
        assertEquals(BigDecimal("1.000000000"), BigDecimal(Math.PI / 2).sin(10))
        assertEquals(BigDecimal("5.222398133E-10"), BigDecimal(Math.PI).sin(10))

        assertEquals(BigDecimal("0.94"), BigDecimal("1.2").sin(2))
        assertEquals(BigDecimal("0.9320390860"), BigDecimal("1.2").sin(10))
    }

    @Test
    fun cos() {
        assertEquals(BigDecimal("0.9999995000"), BigDecimal(0.001).cos(10))
        assertEquals(BigDecimal("1.384383285E-10"), BigDecimal(Math.PI / 2).cos(10))
        assertEquals(BigDecimal("-1.000000001"), BigDecimal(Math.PI).cos(10))
    }

    @Test
    fun tan() {
        assertEquals(BigDecimal("0.001000000333"), BigDecimal(0.001).tan(10))
        assertEquals(BigDecimal("7223433068"), BigDecimal(Math.PI / 2).tan(10))
        assertEquals(BigDecimal("-5.222398128E-10"), BigDecimal(Math.PI).tan(10))
    }

    // TODO sinh/cosh

    @Test
    fun tanh() {
        assertEquals(BigDecimal("0.761594155955"), BigDecimal(1.0).tanh(12))
    }

    // TODO all 6 arc cases
}