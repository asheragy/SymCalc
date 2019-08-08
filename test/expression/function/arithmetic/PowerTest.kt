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
        val zeros = arrayOf(IntegerNum.ZERO, Rational.ZERO, RealNum.create(0.0), RealNum_BigDecimal(BigDecimal.ZERO), ComplexNum(0,0))
        val nums = arrayOf(IntegerNum(5), Rational(2,3), RealNum.create(3.14), RealNum_BigDecimal(BigDecimal.TEN), ComplexNum(7,9))

        for(num in nums) {
            for (zero in zeros) {
                assertTrue("Power($num,$zero)", Power(num, zero).eval().asNumber().isOne)
            }
        }
    }

    @Test
    fun toPowerOne() {
        val ones = arrayOf(IntegerNum.ONE, Rational.ONE, RealNum.create(1.0), RealNum_BigDecimal(BigDecimal.ONE), ComplexNum(1,0))
        val nums = arrayOf(IntegerNum(5), Rational(2,3), RealNum.create(3.14), RealNum_BigDecimal(BigDecimal.TEN), ComplexNum(7,9))

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
        assertEquals(ComplexNum(0,-1), Power(E(), Times(I(), Times(Pi(), Rational(3,2)))).eval())
    }

    @Test
    fun toConstant() {
        assertEquals(Power(IntegerNum(3), Pi()), Power(IntegerNum(3), Pi()).eval())
        assertEquals(Power(Rational(2,3), Pi()), Power(Rational(2,3), Pi()).eval())
        assertEquals(RealNum.create(31.54428070019754), Power(RealNum.create(3.0), Pi()).eval())
        // TODO assertEquals(Power(IntegerNum(3), Pi()), Power(RealNum_BigDecimal("3.0"), Pi()).eval())
        assertEquals(Power(ComplexNum(3,3), Pi()), Power(ComplexNum(3,3), Pi()).eval())
    }

    @Test
    fun fromConstant() {
        assertEquals(Power(Pi(), IntegerNum(3)), Power(Pi(), IntegerNum(3)).eval())
        assertEquals(Power(Pi(), Rational(2,3)), Power(Pi(), Rational(2,3)).eval())
        assertEquals(RealNum.create(31.006276680299816), Power(Pi(), RealNum.create(3.0)).eval())
        //TODO assertEquals(Power(IntegerNum(3), Pi()), Power(Pi(), RealNum_BigDecimal("3.0")).eval())
        assertEquals(Power(Pi(), ComplexNum(3,3)), Power(Pi(), ComplexNum(3,3)).eval())
    }

    @Test
    fun numberExpr_pow() {
        // More tests in corresponding NumberExpr class, only 1 basic here per type to verify Power() calls it
        assertEquals(IntegerNum(9), Power(IntegerNum(3), IntegerNum.TWO).eval())
    }

    @Test
    fun integerToRational() {
        assertEquals(IntegerNum(4), Power(IntegerNum(16), Rational(1,2)).eval())
        assertEquals(IntegerNum(3125), Power(IntegerNum(125), Rational(5,3)).eval())
        assertEquals(Rational(IntegerNum.ONE, IntegerNum(3125)), Power(IntegerNum(125), Rational(-5,3)).eval())
        assertEquals(IntegerNum(7), Power(IntegerNum(16807), Rational(1,5)).eval())

        // Not able to fully evaluate
        assertEquals(Power(IntegerNum(3), Rational(1,3)), Power(IntegerNum(3), Rational(1,3)).eval())
        assertEquals(Power(IntegerNum(29), Rational(2,3)), Power(IntegerNum(29), Rational(2,3)).eval())
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
    fun rationalToInteger() {
        assertEquals(Rational(1,8), Power(Rational.HALF, IntegerNum(3)).eval())
        assertEquals(Rational(1048576, 9765625), Power(Rational(4,5), IntegerNum(10)).eval())
        assertEquals(Rational(9765625, 1048576), Power(Rational(4,5), IntegerNum(-10)).eval())
        assertEquals(Rational(IntegerNum.ONE, IntegerNum("4294967296")), Power(Rational(1,2), IntegerNum(32)).eval())
        assertEquals(Rational(IntegerNum.ONE, IntegerNum("340282366920938463463374607431768211456")), Power(Rational(1,2), IntegerNum(128)).eval())
    }

    @Test
    fun rationalToRational() {
        assertEquals(Divide(IntegerNum.ONE, Power(IntegerNum.TWO,Rational.HALF)), Power(Rational(1,2), Rational(1,2)).eval())
        assertEquals(Rational(4,9), Power(Rational(8,27), Rational(2,3)).eval())
        assertEquals(IntegerNum(2), Power(Rational(1,4), Rational(-1,2)).eval())
        assertEquals(Rational.HALF, Power(Rational(1,4), Rational(1,2)).eval())

        // TODO equal but should simplify to the same expression
        //assertEquals(Power(IntegerNum.TWO, RationalNum.HALF), Power(RationalNum.HALF, RationalNum.HALF.unaryMinus()).eval())
    }

    @Test
    fun rationalToReal() {
        assertEquals(RealNum.create(0.1088188204120155), Power(Rational.HALF, RealNum.create(3.2)).eval())
        assertEquals(RealNum.create(9.18958683997628), Power(Rational.HALF, RealNum.create(-3.2)).eval())

        // TODO need BigDecimal power
        //assertEquals(RealNum_BigDecimal("0.11"), Power(RationalNum.HALF, RealNum_BigDecimal("3.1")).eval())
        //assertEquals(RealNum_BigDecimal("0.1133147323"), Power(RationalNum.HALF, RealNum_BigDecimal("3.141592654")).eval())
        //assertEquals(RealNum_BigDecimal("8.82497783"), Power(RationalNum.HALF, RealNum_BigDecimal("-3.141592654")).eval())
    }

    @Test
    fun rationalToComplex() {
        // No imaginary
        assertEquals(Rational(1,16), Power(Rational.HALF, ComplexNum(4,0)).eval())

        assertEquals(ComplexNum(0.011466060921456358, -0.061439233775702734), N(Power(Rational.HALF, ComplexNum(4,2))).eval())
        // TODO 2.93531 + 15.7284 I
        // Fixed elsewhere, same as some other cases
        //assertEquals(ComplexNum(xx, xx), Power(RationalNum.HALF, ComplexNum(-4.0,-2.0)).eval())

        // Cannot eval
        assertEquals(Power(Rational.HALF, ComplexNum(4,2)), Power(Rational.HALF, ComplexNum(4,2)).eval())
    }

    @Test
    fun realToInteger() {
        assertEquals(RealNum.create(625.0), Power(RealNum.create(5.0), IntegerNum(4)).eval())

        // BigDecimal
        assertEquals(RealNum_BigDecimal("1.0002468"), Power(RealNum_BigDecimal("1.0001234"), IntegerNum.TWO).eval())
        assertEquals(RealNum_BigDecimal("93648.04760"), Power(RealNum_BigDecimal("3.141592654"), IntegerNum(10)).eval())
        assertEquals(RealNum_BigDecimal("8769956796.08269947460"), Power(N(Pi(),20), IntegerNum(20)).eval())
        assertEquals(RealNum_BigDecimal("3.40282366920938463463374607431768211E+38"), Power(RealNum.create("2.00000000000000000000000000000000000"), IntegerNum(128)).eval())
    }

    @Test
    fun realToRational() {
        assertEquals(RealNum.create(2.23606797749979), Power(RealNum.create(5.0), Rational(1,2)).eval())
        assertEquals(RealNum.create(2040886.0816112224), Power(RealNum.create(1.2345), Rational(12345,179)).eval())
        assertEquals(RealNum.create(4.89983252377579E-7), Power(RealNum.create(1.2345), Rational(-12345,179)).eval())
        // TODO not sure why this does not work ~1.37163
        //assertEquals(RealNum.create(1.0), Power(RealNum.create(-1.2345), RationalNum(3,2)).eval())

        assertEquals(RealNum_BigDecimal("2"), Power(RealNum_BigDecimal("4.0"), Rational.HALF).eval())
        assertEquals(RealNum_BigDecimal("1.00006170"), Power(RealNum_BigDecimal("1.0001234"), Rational(1,2)).eval()) // square root

        // TODO not implemented yet, add more tests
        //assertEquals(RealNum_BigDecimal("2"), Power(RealNum_BigDecimal("4.0"), RationalNum(1,3)).eval())
        //assertEquals(RealNum_BigDecimal("0.00008227"), RealNum_BigDecimal("0.0001234").power(RationalNum(3,4)))
    }

    @Test
    fun realToReal() {
        // Double/Double
        assertEquals(RealNum.create(55.90169943749474), Power(RealNum.create(5.0), RealNum.create(2.5)).eval())
        assertEquals(RealNum.create(125.0), Power(RealNum.create(5.0), RealNum.create(BigDecimal(3.0))).eval())

        // Double/BigDecimal
        assertEquals(RealNum.create(36.33783888017471), Power(RealNum_BigDecimal("3.14"), RealNum.create(3.14)).eval())
        assertEquals(RealNum.create(36.33783888017471), Power(RealNum.create(3.14), RealNum_BigDecimal("3.14")).eval())

        // TODO Needs some type of power formula
        //assertEquals(RealNum_BigDecimal("36.x"), Power(RealNum_BigDecimal("3.14"), RealNum_BigDecimal("3.14")).eval())
    }

    @Test
    fun realToComplex() {
        val piBigDec = RealNum_BigDecimal("3.14")

        // Zero imaginary
        assertEquals(RealNum.create(100.0), Power(RealNum.create(10.0), ComplexNum(2,0)).eval())
        assertEquals(RealNum_BigDecimal("9.86"), Power(piBigDec, ComplexNum(2,0)).eval())

        // Double
        assertEquals(ComplexNum(24.70195964872899, 3.848790655850832), Power(RealNum.create(5.0), ComplexNum(2,4)).eval())
        assertEquals(ComplexNum(1.5502967700299068, 1.6113906803859945), Power(RealNum.create(5.0), ComplexNum(Rational.HALF,Rational.HALF)).eval())
        assertEquals(ComplexNum(24.70195964872899, 3.848790655850832), Power(RealNum.create(5.0), ComplexNum(2.0,4.0)).eval())
        assertEquals(ComplexNum(52.40487058561866, -147.56137608427574), Power(RealNum.create(5.0), ComplexNum(piBigDec,piBigDec)).eval())

        // BigDec
        val bigDec = RealNum_BigDecimal("5.0001")
        // TODO none working but multiple reasons
        //assertEquals(ComplexNum(24.70195964872899, 3.848790655850832), Power(bigDec, ComplexNum(2,4)).eval())
        //assertEquals(ComplexNum(1.5502967700299068, 1.6113906803859945), Power(bigDec, ComplexNum(RationalNum.HALF,RationalNum.HALF)).eval())
        //assertEquals(ComplexNum(24.70195964872899, 3.848790655850832), Power(bigDec, ComplexNum(2.0,4.0)).eval())
        //assertEquals(ComplexNum(52.40487058561866, -147.56137608427574), Power(bigDec, ComplexNum(piBigDec,piBigDec)).eval())
    }

    @Test
    fun complexToInteger() {
        // TODO need to implement this as formula, may work on others too which is good
        //assertEquals(IntegerNum(4096), Power(ComplexNum(1,1), IntegerNum(24)).eval())
        //assertEquals(ComplexNum(4096, 4096), Power(ComplexNum(1,1), IntegerNum(25)).eval())
    }

}