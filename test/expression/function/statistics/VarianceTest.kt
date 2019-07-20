package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.assertEqual
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum
import org.cerion.symcalc.expression.number.RealNum
import org.cerion.symcalc.listOfNumbers
import org.junit.Assert.assertEquals
import org.junit.Test

class VarianceTest {

    @Test
    fun basic() {
        val numbers = arrayOf(1.21, 3.4, 2.0, 4.66, 1.5, 5.61, 7.22)
        val list = ListExpr()
        for (number in numbers)
            list.add(RealNum.create(number))

        assertEqual(5.16122380952381, Variance(list).eval())
    }

    @Test
    fun integer() {
        val list = listOfNumbers(600, 470, 170, 430, 300)
        assertEquals(IntegerNum(27130), Variance(list).eval())
    }

    @Test
    fun real() {
        var list = listOfNumbers(1.1, 1.2, 1.3)
        val e = Variance(list).eval()
        assertEquals(RealNum.create(0.01).toDouble(), (e as RealNum).toDouble(), 0.000000001)

        list = listOfNumbers(1.21, 3.4, 2.0, 4.66, 1.5, 5.61, 7.22)
        assertEqual(5.16122380952381, Variance(list).eval())
    }

    @Test
    fun integers_evalAsRational() {
        val list = listOfNumbers(9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4)
        assertEquals(RationalNum(178,19), Variance(list).eval())
    }

}