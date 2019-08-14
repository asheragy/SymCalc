package expression.function.statistics

import org.cerion.symcalc.assertEqual
import org.cerion.symcalc.expression.function.arithmetic.Sqrt
import org.cerion.symcalc.expression.function.statistics.StandardDeviation
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealNum_Double
import org.cerion.symcalc.listOfNumbers
import org.junit.Assert.assertEquals
import org.junit.Test

class StandardDeviationTest {

    @Test
    fun integers_evalAsRational() {
        val list = listOfNumbers(9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4)
        assertEquals(Sqrt(Rational(178,19)).eval(), StandardDeviation(list).eval())
    }

    @Test
    fun real() {
        val list = listOfNumbers(1.21, 3.4, 2.0, 4.66, 1.5, 5.61, 7.22)
        assertEquals(RealNum_Double(2.271832698400965), StandardDeviation(list).eval())
    }
}