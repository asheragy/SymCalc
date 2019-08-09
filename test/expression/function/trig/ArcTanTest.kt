package expression.function.trig

import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.*
import org.junit.Test

class ArcTanTest {

    @Test
    fun basic() {
        assertEquals(Divide(Pi(), IntegerNum(4)), ArcTan(IntegerNum.ONE).eval())
    }
}