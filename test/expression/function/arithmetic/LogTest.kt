package expression.function.arithmetic

import org.cerion.symcalc.expression.function.arithmetic.Log
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LogTest {

    @Test
    fun basic() {
        assertEquals(RealDouble(0.6931471805599453), Log(RealDouble(2.0)).eval())
        assertEquals(RealDouble(-9.210340371976182), Log(RealDouble(0.0001)).eval())
    }

    // TODO_LP add edge cases like 0/-negatives/variations of E/powers

    @Test
    fun negative() {

    }

    @Test
    fun bigDecimal() {
        assertEquals(RealBigDec("0.7"), Log(RealBigDec("2.0")).eval())
        assertEquals(RealBigDec("0.693148"),   Log(RealBigDec("2.000001")).eval())
        assertEquals(RealBigDec("2.3025852"),  Log(RealBigDec("10.000001")).eval())
        assertEquals(RealBigDec("4.60517020"), Log(RealBigDec("100.000001")).eval())
    }
}