package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PiTest {

    @Test
    fun basic() {
        assertEquals(Pi(), Pi().eval())
        assertEquals(RealDouble(3.141592653589793), N(Pi()).eval())

        assertEquals(RealBigDec("3.142"), N(Pi(), Integer(4)).eval())
        assertEquals(RealBigDec("3.1416"), N(Pi(), Integer(5)).eval())

        assertEquals(RealBigDec("3.1415926535897932385"), N(Pi(), Integer(20)).eval())
        assertEquals(RealBigDec("3.1415926535897932384626433832795028841971693993751"), N(Pi(), Integer(50)).eval())
        assertEquals(RealBigDec("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170680"), N(Pi(), Integer(100)).eval())
    }

    @Test
    fun digitsTest() {
        // Check Nth digit for accuracy
        val checkDigit : (Int, Int) -> Unit = { pos, n ->
            val test = N(Pi(), Integer(pos+2)).eval() as RealBigDec
            val digit = test.value.toString().substring(pos+1,pos+2)
            assertEquals(n.toString(), digit, "expected $n at position $pos")
        }

        checkDigit(1, 1)
        checkDigit(2, 4)
        checkDigit(3, 1)
        checkDigit(4, 5)

        checkDigit(10, 5)
        checkDigit(25, 3)
        checkDigit(50, 0)
        checkDigit(100, 9)
        checkDigit(200, 6)
        //checkDigit(500, 2)
        //checkDigit(1000, 9)

        //checkDigit(5000, 1) // too slow at this point
        //checkDigit(10000, 8)
        //checkDigit(100000, 6)
        //checkDigit(1000000, 1)
        //checkDigit(10000000, 7)
        //checkDigit(12345678, 2) // slow even in mathematica
    }
}