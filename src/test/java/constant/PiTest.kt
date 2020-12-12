package org.cerion.symcalc.constant

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.function.core.N
import kotlin.test.Test
import kotlin.test.assertEquals

class PiTest {

    @Test
    fun basic() {
        assertAll(
                Pi() `==` Pi(),
                N(Pi()) `==` 3.141592653589793)
    }

    @Test
    fun realBigDec() {
        assertAll(
                Pi().eval(1) `==` "3.0",
                Pi().eval(3) `==` "3.14",
                Pi().eval(100) `==` "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170680")
    }

    @Test
    fun compute_first50digits() {
        val pi50 = "3.1415926535897932384626433832795028841971693993751"

        for(i in 2..50) {
            val expected = pi50.substring(0, i + 1)
            val actual = Pi().evalCompute(i+2) // Add 2 extra digits to compensate for string not being rounded
            assertEquals(expected, actual.toString().substring(0, i + 1))
        }
    }

    @Test
    fun rounding() {
        // The last digit rounded up from full pi value
        assertAll(
                Pi().eval(4) `==` "3.142",
                Pi().eval(5) `==` "3.1416",
                Pi().eval(12) `==` "3.14159265359",
                Pi().eval(20) `==` "3.1415926535897932385")

        // Not rounded
        assertAll(
                Pi().eval(2) `==` "3.1",
                Pi().eval(19) `==` "3.141592653589793238")
    }

    @Test
    fun digitsTest() {
        // Check Nth digit for accuracy
        val checkDigit : (Int, Int) -> Unit = { precision, n ->
            val eval = Pi().eval(precision) as RealBigDec
            val digit = eval.toString().substringBefore('`').last().toString()
            assertEquals(n.toString(), digit, "expected $n at position $precision")
        }

        checkDigit(100, 8)
        checkDigit(200, 0)
        checkDigit(500, 1)

        // These are computed and start to get slow
        //checkDigit(1000, 9)
        //checkDigit(5000, 2)
        //checkDigit(10000, 8) // ~5 seconds
        //checkDigit(20000, 8) // ~20 seconds
        //checkDigit(30000, 8) // ~1 minute
        //checkDigit(40000, 5)
        //checkDigit(100000, 5)
        //checkDigit(1000000, 5)
    }
}