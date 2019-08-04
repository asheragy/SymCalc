package expression.number

import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.Assert.assertEquals
import org.junit.Test

class RealNum_BigDecimalTest {

    @Test
    fun precision() {
        assertEquals(11, RealNum_BigDecimal("1.0000000001").precision)
        assertEquals(1, RealNum_BigDecimal("0.0000000001").precision)
        assertEquals(2, RealNum_BigDecimal("0.0000000011").precision)
        assertEquals(13, RealNum_BigDecimal("111.0000000011").precision)
    }

    @Test
    fun precision_evaluate() {
        assertEquals(RealNum_BigDecimal("111.000000001"), RealNum_BigDecimal("111.0000000011").evaluate(12))
    }

    @Test
    fun precision_lowestOfTwo() {
        val a = RealNum_BigDecimal("0.33")
        val b = RealNum_BigDecimal("0.3333333333")

        // PrecisionA + PrecisionB = Precision of lowest
        assertEquals(RealNum_BigDecimal("0.66"), a + b)
        assertEquals(RealNum_BigDecimal("0.00"), a - b)
        assertEquals(RealNum_BigDecimal("0.11"), a * b)
        assertEquals(RealNum_BigDecimal("0.99"), a / b)

        // using N()
        assertEquals(RealNum_BigDecimal("5.85987"), Plus(N(E(), IntegerNum(10)), N(Pi(), IntegerNum(5))).eval())
    }

    @Test
    fun multiply() {
        assertEquals(RealNum_BigDecimal("0.0002468"), RealNum_BigDecimal("0.0001234") * IntegerNum.TWO)
        assertEquals(RealNum_BigDecimal("0.0001851"), RealNum_BigDecimal("0.0001234") * RationalNum(3,2))
        assertEquals(RealNum.create(0.0037427219999999995), RealNum_BigDecimal("0.0001234") * RealNum.create(30.33))
        assertEquals(RealNum_BigDecimal("0.003743"), RealNum_BigDecimal("0.0001234") * RealNum_BigDecimal("30.33"))
        assertEquals(ComplexNum(RealNum_BigDecimal("0.0002468"),RealNum_BigDecimal("0.0003702")), RealNum_BigDecimal("0.0001234") * ComplexNum(2,3))
    }

    @Test
    fun divide() {
        assertEquals(RealNum_BigDecimal("0.0000617"), RealNum_BigDecimal("0.0001234") / IntegerNum.TWO)
        assertEquals(RealNum_BigDecimal("0.00008227"), RealNum_BigDecimal("0.0001234") / RationalNum(3,2))
        assertEquals(RealNum_BigDecimal("0.0329747"), RealNum_BigDecimal("1.0001234") / RealNum.create(30.33))
        assertEquals(RealNum_BigDecimal("0.03297"), RealNum_BigDecimal("1.0001234") / RealNum_BigDecimal("30.33"))
        assertEquals(ComplexNum(RealNum_BigDecimal("0.00001898"),RealNum_BigDecimal("-0.00002848")), RealNum_BigDecimal("0.0001234") / ComplexNum(2,3))
    }

    @Test
    fun power() {
        assertEquals(RealNum_BigDecimal("1.0002468"), RealNum_BigDecimal("1.0001234").power(IntegerNum.TWO))
        assertEquals(RealNum_BigDecimal("1.00006170"), RealNum_BigDecimal("1.0001234").power(RationalNum(1,2))) // square root

        // Needs Nth root formula
        //assertEquals(RealNum_BigDecimal("0.00008227"), RealNum_BigDecimal("0.0001234").power(RationalNum(3,4)))

        assertEquals(RealNum.create("4.028321734216929"), RealNum_BigDecimal("2.0001234").power(RealNum.create(2.01)))

        // Needs some type of power formula
        //assertEquals(RealNum_BigDecimal("0.03297"), RealNum_BigDecimal("1.0001234") / RealNum_BigDecimal("30.33"))

        // Implemented as complex ^ complex
        //assertEquals(ComplexNum(RealNum_BigDecimal("0.00001898"),RealNum_BigDecimal("-0.00002848")), RealNum_BigDecimal("0.0001234") / ComplexNum(2,3))
    }
}