package expression.function.arithmetic

import expression.constant.I
import expression.function.logical.Equal
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class PowerTest {

    @Test
    fun toPowerZero() {
        val zeros = arrayOf(IntegerNum.ZERO, RationalNum.ZERO, RealNum.create(0.0), ComplexNum(0,0))
        val nums = arrayOf(IntegerNum(5), RationalNum(2,3), RealNum.create(3.14), RealNum_BigDecimal(BigDecimal.TEN), ComplexNum(7,9))

        for(num in nums) {
            for (zero in zeros) {
                assertTrue("Power($num,$zero)", Power(num, zero).eval().asNumber().isOne)
            }
        }
    }

    @Test
    fun toPoewrOne() {
        val ones = arrayOf(IntegerNum.ONE, RationalNum.ONE, RealNum.create(1.0), ComplexNum(1,0))
        val nums = arrayOf(IntegerNum(5), RationalNum(2,3), RealNum.create(3.14), ComplexNum(7,9))

        for(num in nums) {
            for (one in ones) {
                val power = Power(num, one).eval()
                assertTrue("Power($num,$one)", Equal(num, power).eval().asBool().value)
            }
        }
    }

    @Test
    fun ePiI_approx() {
        val approx = ComplexNum(RealNum.create(-1.0), RealNum.create(1.2246467991473532E-16))
        assertEquals(approx, Power(N(E()), Times(N(Pi()), I())).eval())
        assertEquals(approx, Power(N(E()), Times(Pi(), I())).eval())
        assertEquals(approx, Power(E(), Times(N(Pi()), I())).eval())
    }

    @Test
    fun ePiI() {
        assertEquals(IntegerNum(-1), Power(E(), Times(Pi(), I())).eval())
        assertEquals(IntegerNum(-1), Power(E(), Times(I(), Pi())).eval())

        // Variations
        assertEquals(Times(IntegerNum(-1),Power(E(), IntegerNum(5))), Power(E(), Plus(IntegerNum(5), Times(I(), Pi()))).eval())
        assertEquals(IntegerNum.ONE, Power(E(), Times(IntegerNum.TWO, I(), Pi())).eval())
        assertEquals(ComplexNum(0,1), Power(E(), Times(I(), Divide(Pi(), IntegerNum.TWO))).eval())
        assertEquals(ComplexNum(0,-1), Power(E(), Times(I(), Times(Pi(), RationalNum(3,2)))).eval())
    }

    // TODO number types, add at least 1 basic test for all possibilities
    @Test
    fun integer_realAndComplexConverted() {
        // Integer is converted to these values and power() is evaluated in those classes, minimal testing needed for this class
        val three = IntegerNum(3)
        val real = RealNum.create(3.14)
        val realBigDec = RealNum.create(BigDecimal("2.35340583128859694839201928385968473749596868726265"))
        //val complex = ComplexNum(2,3)

        assertEquals(RealNum.create(31.489135652454948), Power(three, real).eval())
        //assertEquals(IntegerNum.ONE, Power(three, realBigDec).eval())
        //assertEquals(ComplexNum(3,0).power(complex), three.power(complex))
    }

    @Test
    fun toRational() {
        assertEquals(Divide(IntegerNum.ONE, Power(IntegerNum.TWO,RationalNum.HALF)), Power(RationalNum(1,2), RationalNum(1,2)).eval())
    }

    @Test
    fun rational() {
        assertEquals(RationalNum(1,8), Power(RationalNum.HALF, IntegerNum(3)).eval())
        //assertFailsWith<UnsupportedOperationException> { Power(RationalNum.HALF, RationalNum(1,3)).eval() }
        assertEquals(RealNum.create(0.1088188204120155), Power(RationalNum.HALF, RealNum.create(3.2)).eval())
        assertEquals(RationalNum(1,16), Power(RationalNum.HALF, ComplexNum(4,0)).eval())
        //assertFailsWith<UnsupportedOperationException> { Power(RationalNum.HALF,ComplexNum(1,1)).eval() }
    }

    @Test
    fun real_double() {
        assertEquals(RealNum.create(625.0), Power(RealNum.create(5.0), IntegerNum(4)).eval())
        assertEquals(RealNum.create(2.23606797749979), Power(RealNum.create(5.0), RationalNum(1,2)).eval())
        assertEquals(RealNum.create(55.90169943749474), Power(RealNum.create(5.0), RealNum.create(2.5)).eval())
        assertEquals(RealNum.create(125.0), Power(RealNum.create(5.0), RealNum.create(BigDecimal(3.0))).eval())
        assertEquals(RealNum.create(100.0), Power(RealNum.create(10.0), ComplexNum(2,0)).eval())
        assertEquals(ComplexNum(RealNum.create(24.70195964872899), RealNum.create(3.848790655850832)), Power(RealNum.create(5.0), ComplexNum(2,4)).eval())
    }

    @Test
    fun real_BigDec() {
        assertEquals(RealNum_BigDecimal("1.0002468"), Power(RealNum_BigDecimal("1.0001234"), IntegerNum.TWO).eval())
        assertEquals(RealNum_BigDecimal("1.00006170"), Power(RealNum_BigDecimal("1.0001234"), RationalNum(1,2)).eval()) // square root

        // Needs Nth root formula
        //assertEquals(RealNum_BigDecimal("0.00008227"), RealNum_BigDecimal("0.0001234").power(RationalNum(3,4)))

        assertEquals(RealNum.create("4.028321734216929"), Power(RealNum_BigDecimal("2.0001234"), RealNum.create(2.01)).eval())

        // Needs some type of power formula
        //assertEquals(RealNum_BigDecimal("0.03297"), RealNum_BigDecimal("1.0001234") / RealNum_BigDecimal("30.33"))

        // Implemented as complex ^ complex
        //assertEquals(ComplexNum(RealNum_BigDecimal("0.00001898"),RealNum_BigDecimal("-0.00002848")), RealNum_BigDecimal("0.0001234") / ComplexNum(2,3))
    }

    @Test
    fun complex() {
        assertEquals(ComplexNum(RealNum.create(-1.947977671863125), RealNum.create(3.493620327099486)), N(Power(IntegerNum(2), ComplexNum(2,3))).eval())
    }
}