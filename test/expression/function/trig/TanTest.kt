package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Assert.*
import org.junit.Test

class TanTest {

    @Test
    fun basic() {
        val tan = Tan(IntegerNum(5))
        var eval = tan.eval()

        // Eval does nothing
        assertEquals(tan, eval)

        // Evals to number
        val tan2 = N(tan)
        eval = tan2.eval()
        assertTrue("not a number", eval.isNumber)

        assertEquals(RealDouble(-3.380515006246586), eval)
    }
}

