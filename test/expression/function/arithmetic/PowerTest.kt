package expression.function.arithmetic

import expression.constant.I
import expression.function.logical.Equal
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class PowerTest {

    @Test
    fun toPowerZero() {
        val zeros = arrayOf(IntegerNum.ZERO, RationalNum.ZERO, RealNum.create(0.0), RealNum_BigDecimal(BigDecimal.ZERO), ComplexNum(0,0))
        val nums = arrayOf(IntegerNum(5), RationalNum(2,3), RealNum.create(3.14), RealNum_BigDecimal(BigDecimal.TEN), ComplexNum(7,9))

        for(num in nums) {
            for (zero in zeros) {
                assertTrue("Power($num,$zero)", Power(num, zero).eval().asNumber().isOne)
            }
        }
    }

    @Test
    fun toPowerOne() {
        val ones = arrayOf(IntegerNum.ONE, RationalNum.ONE, RealNum.create(1.0), RealNum_BigDecimal(BigDecimal.ONE), ComplexNum(1,0))
        val nums = arrayOf(IntegerNum(5), RationalNum(2,3), RealNum.create(3.14), RealNum_BigDecimal(BigDecimal.TEN), ComplexNum(7,9))

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

    @Test
    fun toConstant() {
        assertEquals(Power(IntegerNum(3), Pi()), Power(IntegerNum(3), Pi()).eval())
        assertEquals(Power(RationalNum(2,3), Pi()), Power(RationalNum(2,3), Pi()).eval())
        assertEquals(RealNum.create(31.54428070019754), Power(RealNum.create(3.0), Pi()).eval())
        // TODO assertEquals(Power(IntegerNum(3), Pi()), Power(RealNum_BigDecimal("3.0"), Pi()).eval())
        assertEquals(Power(ComplexNum(3,3), Pi()), Power(ComplexNum(3,3), Pi()).eval())
    }

    @Test
    fun fromConstant() {
        assertEquals(Power(Pi(), IntegerNum(3)), Power(Pi(), IntegerNum(3)).eval())
        assertEquals(Power(Pi(), RationalNum(2,3)), Power(Pi(), RationalNum(2,3)).eval())
        assertEquals(RealNum.create(31.006276680299816), Power(Pi(), RealNum.create(3.0)).eval())
        //TODO assertEquals(Power(IntegerNum(3), Pi()), Power(Pi(), RealNum_BigDecimal("3.0")).eval())
        assertEquals(Power(Pi(), ComplexNum(3,3)), Power(Pi(), ComplexNum(3,3)).eval())
    }

    @Test
    fun integerToInteger() {
        assertEquals(IntegerNum(-243), Power(IntegerNum(-3), IntegerNum(5)).eval())
        assertEquals(IntegerNum("910043815000214977332758527534256632492715260325658624"), Power(IntegerNum(12), IntegerNum(50)).eval())
        assertEquals(RationalNum(IntegerNum.ONE, IntegerNum(243)), Power(IntegerNum(3), IntegerNum(-5)).eval())
    }

    @Test
    fun integerToRational() {
        assertEquals(IntegerNum(4), Power(IntegerNum(16), RationalNum(1,2)).eval())
        assertEquals(IntegerNum(3125), Power(IntegerNum(125), RationalNum(5,3)).eval())
        assertEquals(RationalNum(IntegerNum.ONE, IntegerNum(3125)), Power(IntegerNum(125), RationalNum(-5,3)).eval())
        assertEquals(IntegerNum(7), Power(IntegerNum(16807), RationalNum(1,5)).eval())

        // Not able to fully evaluate
        assertEquals(Power(IntegerNum(3), RationalNum(1,3)), Power(IntegerNum(3), RationalNum(1,3)).eval())
        assertEquals(Power(IntegerNum(29), RationalNum(2,3)), Power(IntegerNum(29), RationalNum(2,3)).eval())
        // TODO need to factor denominator and reduce
        //assertEquals(Power(IntegerNum(23), RationalNum(1,2)), Power(IntegerNum(529), RationalNum(1,4)).eval())
    }

    @Test
    fun integerToReal() {
        assertEquals(RealNum.create(15.588457268119896), Power(IntegerNum(3), RealNum.create(2.5)).eval())
        assertEquals(RealNum.create(0.001520667150293348), Power(IntegerNum(223), RealNum.create(-1.2)).eval())
        assertEquals(RealNum.create(5888.436553555892), Power(IntegerNum("100000000000000000000000000000"), RealNum.create(0.13)).eval())
        assertEquals(RealNum.create(1.1437436793461719E257), Power(IntegerNum(123), RealNum.create(123.0)).eval())
        assertEquals(RealNum.create(31.489135652454948), Power(IntegerNum(3), RealNum.create(3.14)).eval())

        // Big Decimal
        // TODO need custom function for this
        //assertEquals(RealNum_BigDecimal("31.54428070019754396"), Power(IntegerNum(3), RealNum.create(BigDecimal("3.1415926535897932385"))).eval())
        //assertEquals(RealNum.create(BigDecimal("13.269664513862131105721175550585075831469346963616")), Power(three, RealNum.create(BigDecimal("2.35340583128859694839201928385968473749596868726265"))).eval())
    }

    @Test
    fun integerToComplex() {
        // imaginary zero
        assertEquals(IntegerNum(32), Power(IntegerNum(2), ComplexNum(5,0)).eval())

        // Cannot fully evaluate
        assertEquals(Power(IntegerNum(2), ComplexNum(2,3)), Power(IntegerNum(2), ComplexNum(2,3)).eval())

        assertEquals(ComplexNum(RealNum.create(-0.48699441796578125), RealNum.create(0.8734050817748715)), N(Power(IntegerNum(2), ComplexNum(0,3))).eval())
        assertEquals(ComplexNum(RealNum.create(-1.947977671863125), RealNum.create(3.493620327099486)), N(Power(IntegerNum(2), ComplexNum(2,3))).eval())
        // TODO this should work but is probably not a Power() issue
        //assertEquals(ComplexNum(RealNum.create(-1.947977671863125), RealNum.create(3.493620327099486)), Power(IntegerNum(2), ComplexNum(RealNum.create(2.0),RealNum.create(3.0))).eval())
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
}