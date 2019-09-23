package expression.function.numeric

import org.cerion.symcalc.expression.function.numeric.Floor
import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FloorTest {

    @Test
    fun basic() {
        // NumberExpr handled by classes so not really anything to do here
        assertEquals(Integer(5), Floor(RealDouble(5.00000000001)).eval())
    }
}