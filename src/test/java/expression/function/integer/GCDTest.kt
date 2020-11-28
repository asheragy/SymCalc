package expression.function.integer

import org.cerion.symcalc.expression.function.integer.GCD
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals

class GCDTest {

    @Test
    fun basic() {
        assertEquals(Integer(1), GCD(Integer(2), Integer(3)).eval())
        assertEquals(Integer(2), GCD(Integer(8), Integer(10)).eval())
    }

    @Test
    fun multipleValues() {
        assertEquals(Integer(8), GCD(Integer(8), Integer(16), Integer(24)).eval())
        assertEquals(Integer(4), GCD(Integer(8), Integer(16), Integer(24), Integer(4)).eval())
        assertEquals(Integer(1), GCD(Integer(10), Integer(100), Integer(3), Integer(1000)).eval())
    }
}