package expression.function.core

import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.number.Integer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HoldTest {

    @Test
    fun flatPropertyHeld() {
        val e = Hold(Plus(Integer.ONE, Plus(Integer.TWO, Integer.ZERO))).eval()
        assertEquals(2, e[0].size)
    }
}