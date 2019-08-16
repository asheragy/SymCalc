package expression.function.trig

import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.number.Integer
import org.junit.Assert.*
import org.junit.Test

class ArcTanTest {

    @Test
    fun basic() {
        assertEquals(Divide(Pi(), Integer(4)), ArcTan(Integer.ONE).eval())
    }
}