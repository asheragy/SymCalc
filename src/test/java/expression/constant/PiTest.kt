package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals

class PiTest {

    @Test
    fun basic() {
        assertEquals(Pi(), Pi().eval())
        assertEquals(RealDouble(3.141592653589793), N(Pi()).eval())

        assertEquals(RealBigDec("3.0"), N(Pi(), Integer(1)).eval())
        assertEquals(RealBigDec("3.14"), N(Pi(), Integer(3)).eval())
        assertEquals(RealBigDec("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170680"), N(Pi(), Integer(100)).eval())
    }

    @Test
    fun rounding() {
        // The last digit rounded up from full pi value
        assertEquals(RealBigDec("3.142"), N(Pi(), Integer(4)).eval())
        assertEquals(RealBigDec("3.1416"), N(Pi(), Integer(5)).eval())
        assertEquals(RealBigDec("3.14159265359"), N(Pi(), Integer(12)).eval())
        assertEquals(RealBigDec("3.1415926535897932385"), N(Pi(), Integer(20)).eval())

        // Not rounded
        assertEquals(RealBigDec("3.1"), N(Pi(), Integer(2)).eval())
        assertEquals(RealBigDec("3.141592653589793238"), N(Pi(), Integer(19)).eval())
    }

    @Test
    fun first50digits() {
        val pi50 = "3.1415926535897932384626433832795028841971693993751"

        for(i in 2..50) {
            val expected = pi50.substring(0, i + 1)
            val actual = N(Pi(), Integer(i+2)).eval() // Generate 2 extra digits or rounding will be off in some positions
            assertEquals(expected, actual.toString().substring(0, i + 1))
        }
    }

    @Test
    fun digitsTest() {
        // Check Nth digit for accuracy
        val checkDigit : (Int, Int) -> Unit = { pos, n ->
            val test = N(Pi(), Integer(pos+5)).eval() as RealBigDec
            val digit = test.value.toString().substring(pos+1,pos+2)
            assertEquals(n.toString(), digit, "expected $n at position $pos")
        }

        checkDigit(100, 9)
        checkDigit(200, 6)

        // Keep tests under 50ms
        //checkDigit(500, 2)
        //checkDigit(1000, 9)

        //checkDigit(5000, 1)
        //checkDigit(10000, 8) // ~5 seconds
        //checkDigit(20000, 8) // ~20 seconds
        //checkDigit(30000, 8) // ~1 minute
        //checkDigit(40000, 1)
        //checkDigit(100000, 6)
        //checkDigit(1000000, 1)
        //checkDigit(10000000, 7)
        //checkDigit(12345678, 2) // slow even in mathematica
    }
}