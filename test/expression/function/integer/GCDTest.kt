package expression.function.integer

import org.cerion.symcalc.expression.function.integer.GCD
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.*
import org.junit.Test

class GCDTest {

    @Test
    fun basic() {
        assertEquals(IntegerNum(1), GCD(IntegerNum(2), IntegerNum(3)).eval())
        assertEquals(IntegerNum(2), GCD(IntegerNum(8), IntegerNum(10)).eval())
    }

    @Test
    fun multipleValues() {
        assertEquals(IntegerNum(8), GCD(IntegerNum(8), IntegerNum(16), IntegerNum(24)).eval())
        assertEquals(IntegerNum(4), GCD(IntegerNum(8), IntegerNum(16), IntegerNum(24), IntegerNum(4)).eval())
        assertEquals(IntegerNum(1), GCD(IntegerNum(10), IntegerNum(100), IntegerNum(3), IntegerNum(1000)).eval())
    }
}