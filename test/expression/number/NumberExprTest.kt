package expression.number

import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NumberExprTest {

    @Test
    fun equals() {
        // TODO Integer(1) == Complex(1,0) but for unit tests that may not what we want
        assertEquals(IntegerNum.ONE, ComplexNum(1, 0).eval())
        //assertNotEquals(IntegerNum.ONE, ComplexNum(1, 0))
    }
}