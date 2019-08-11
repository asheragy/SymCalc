package expression.constant

import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.*
import org.junit.Test

class ITest {

    @Test
    fun string() {
        assertEquals("i", I().toString())
    }

    @Test
    fun hold() {
        val hold = Hold(I()).eval()
        assertEquals(I(), hold[0])
    }

    @Test
    fun eval() {
        assertEquals(Complex(0, 1), I().eval())
    }

    @Test
    fun arithmetic() {
        assertEquals(Complex(2, 1), Plus(IntegerNum(2), I()).eval())
        assertEquals(Complex(0, 5), Times(IntegerNum(5), I()).eval())
    }
}