package expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.*
import org.junit.Test

class HoldTest {

    @Test
    fun flatPropertyHeld() {
        val e = Hold(Plus(IntegerNum.ONE, Plus(IntegerNum.TWO, IntegerNum.ZERO))).eval()
        assertEquals(2, e[0].size)
    }
}