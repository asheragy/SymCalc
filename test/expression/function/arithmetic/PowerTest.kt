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
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PowerTest {

    @Test
    fun toPowerZero() {
        val zeros = arrayOf(Integer.ZERO, Rational.ZERO, RealDouble(0.0), RealBigDec(BigDecimal.ZERO), Complex(0,0))
        val nums = arrayOf(Integer(5), Rational(2,3), RealDouble(3.14), RealBigDec(BigDecimal.TEN), Complex(7,9))

        for(num in nums) {
            for (zero in zeros) {
                assertTrue(Power(num, zero).eval().asNumber().isOne, "Power($num,$zero)")
            }
        }
    }

    @Test
    fun toPowerOne() {
        val ones = arrayOf(Integer.ONE, Rational.ONE, RealDouble(1.0), RealBigDec(BigDecimal.ONE), Complex(1,0))
        val nums = arrayOf(Integer(5), Rational(2,3), RealDouble(3.14), RealBigDec(BigDecimal.TEN), Complex(7,9))

        for(num in nums) {
            for (one in ones) {
                val power = Power(num, one).eval()
                assertTrue(Equal(num, power).eval().asBool().value, "Power($num,$one)")
            }
        }
    }

    @Test
    fun ePiI_approx() {
        val approx = Complex(RealDouble(-1.0), RealDouble(1.2246467991473532E-16))
        assertEquals(approx, Power(N(E()), Times(N(Pi()), I())).eval())
        assertEquals(approx, Power(N(E()), Times(Pi(), I())).eval())
        assertEquals(approx, Power(E(), Times(N(Pi()), I())).eval())
    }

    @Test
    fun ePiI() {
        assertEquals(Integer(-1), Power(E(), Times(Pi(), I())).eval())
        assertEquals(Integer(-1), Power(E(), Times(I(), Pi())).eval())

        // Variations
        assertEquals(Times(Integer(-1),Power(E(), Integer(5))), Power(E(), Plus(Integer(5), Times(I(), Pi()))).eval())
        assertEquals(Integer.ONE, Power(E(), Times(Integer.TWO, I(), Pi())).eval())
        assertEquals(Complex(0,1), Power(E(), Times(I(), Divide(Pi(), Integer.TWO))).eval())
        assertEquals(Complex(0,-1), Power(E(), Times(I(), Times(Pi(), Rational(3,2)))).eval())
    }

    @Test
    fun toConstant() {
        assertEquals(Power(Integer(3), Pi()), Power(Integer(3), Pi()).eval())
        assertEquals(Power(Rational(2,3), Pi()), Power(Rational(2,3), Pi()).eval())
        assertEquals(RealDouble(31.54428070019754), Power(RealDouble(3.0), Pi()).eval())
        assertEquals(RealBigDec("36.46"), Power(RealBigDec("3.1415"), Pi()).eval())
        assertEquals(Power(Complex(3,3), Pi()), Power(Complex(3,3), Pi()).eval())
    }

    @Test
    fun fromConstant() {
        assertEquals(Power(Pi(), Integer(3)), Power(Pi(), Integer(3)).eval())
        assertEquals(Power(Pi(), Rational(2,3)), Power(Pi(), Rational(2,3)).eval())
        assertEquals(RealDouble(31.006276680299816), Power(Pi(), RealDouble(3.0)).eval())
        // TODO_LP answer here might be 36.46, not sure if precision of Pi is initially evaluated more than 5 digits
        assertEquals(RealBigDec("36.45"), Power(Pi(), RealBigDec("3.1415")).eval())
        assertEquals(Power(Pi(), Complex(3,3)), Power(Pi(), Complex(3,3)).eval())
    }

    @Test
    fun numberExpr_pow() {
        // More tests in corresponding NumberExpr class, only 1 basic here per type to verify Power() calls it

        // Int to Int
        assertEquals(Integer(9), Power(Integer(3), Integer.TWO).eval())

        // Int to Rational
        assertEquals(Integer(7), Power(Integer(16807), Rational(1,5)).eval())

        // Complex to Int
        assertEquals(Complex(Rational(2823, 16), Rational(6121, 32)), Power(Complex(Integer(3), Rational.HALF), Integer(5)).eval())
        assertEquals(Complex(0.0, 64.36342999999998), Power(Complex(Integer(0), RealDouble(2.3)), Integer(5)).eval())

        // BigDec to BigDec
        assertEquals(RealBigDec("13.26969"), Power(RealBigDec("3.000001"), RealBigDec("2.35340583128859694839201928385968473749596868726265")).eval())
    }

    @Test
    fun integerToReal() {
        assertEquals(RealDouble(15.588457268119896), Power(Integer(3), RealDouble(2.5)).eval())
        assertEquals(RealDouble(0.001520667150293348), Power(Integer(223), RealDouble(-1.2)).eval())
        assertEquals(RealDouble(5888.436553555892), Power(Integer("100000000000000000000000000000"), RealDouble(0.13)).eval())
        assertEquals(RealDouble(1.1437436793461719E257), Power(Integer(123), RealDouble(123.0)).eval())
        assertEquals(RealDouble(31.489135652454948), Power(Integer(3), RealDouble(3.14)).eval())

        // Big Decimal
        assertEquals(RealBigDec("31.54428070019754396"), Power(Integer(3), RealBigDec("3.1415926535897932385")).eval())
    }

    @Test
    fun integerToComplex() {
        // imaginary zero
        assertEquals(Integer(32), Power(Integer(2), Complex(5,0)).eval())

        // Cannot fully evaluate
        assertEquals(Power(Integer(2), Complex(2,3)), Power(Integer(2), Complex(2,3)).eval())

        assertEquals(Complex(RealDouble(-0.48699441796578125), RealDouble(0.8734050817748715)), N(Power(Integer(2), Complex(0,3))).eval())
        assertEquals(Complex(RealDouble(-1.947977671863125), RealDouble(3.493620327099486)), N(Power(Integer(2), Complex(2,3))).eval())
        assertEquals(Complex(RealDouble(-1.947977671863125), RealDouble(3.493620327099486)), Power(Integer(2), Complex(RealDouble(2.0), RealDouble(3.0))).eval())
    }

    @Test
    fun rationalToInteger() {
        assertEquals(Rational(1,8), Power(Rational.HALF, Integer(3)).eval())
        assertEquals(Rational(1048576, 9765625), Power(Rational(4,5), Integer(10)).eval())
        assertEquals(Rational(9765625, 1048576), Power(Rational(4,5), Integer(-10)).eval())
        assertEquals(Rational(Integer.ONE, Integer("4294967296")), Power(Rational(1,2), Integer(32)).eval())
        assertEquals(Rational(Integer.ONE, Integer("340282366920938463463374607431768211456")), Power(Rational(1,2), Integer(128)).eval())
    }

    @Test
    fun rationalToRational() {
        assertEquals(Divide(Integer.ONE, Power(Integer.TWO,Rational.HALF)), Power(Rational(1,2), Rational(1,2)).eval())
        assertEquals(Rational(4,9), Power(Rational(8,27), Rational(2,3)).eval())
        assertEquals(Integer(2), Power(Rational(1,4), Rational(-1,2)).eval())
        assertEquals(Rational.HALF, Power(Rational(1,4), Rational(1,2)).eval())

        assertEquals(Power(Integer.TWO, Rational.HALF), Power(Rational.HALF, Rational.HALF.unaryMinus()).eval())
    }

    @Test
    fun rationalToReal() {
        assertEquals(RealDouble(0.1088188204120155), Power(Rational.HALF, RealDouble(3.2)).eval())
        assertEquals(RealDouble(9.18958683997628), Power(Rational.HALF, RealDouble(-3.2)).eval())

        assertEquals(RealBigDec("0.1133"), Power(Rational.HALF, RealBigDec("3.1415")).eval())
        assertEquals(RealBigDec("0.113314732"), Power(Rational.HALF, RealBigDec("3.141592654")).eval())
        assertEquals(RealBigDec("8.82497783"), Power(Rational.HALF, RealBigDec("-3.141592654")).eval())
    }

    @Test
    fun rationalToComplex() {
        // No imaginary
        assertEquals(Rational(1,16), Power(Rational.HALF, Complex(4,0)).eval())

        assertEquals(Complex(0.011466060921456358, -0.061439233775702734), N(Power(Rational.HALF, Complex(4,2))).eval())
        assertEquals(Complex(2.9353115958928275, 15.7284438465799), Power(Rational.HALF, Complex(-4.0,-2.0)).eval())

        // Cannot eval
        assertEquals(Power(Rational.HALF, Complex(4,2)), Power(Rational.HALF, Complex(4,2)).eval())
    }

    @Test
    fun realToInteger() {
        assertEquals(RealDouble(625.0), Power(RealDouble(5.0), Integer(4)).eval())

        // BigDecimal
        assertEquals(RealBigDec("1.0002468"), Power(RealBigDec("1.0001234"), Integer.TWO).eval())
        assertEquals(RealBigDec("93648.04760"), Power(RealBigDec("3.141592654"), Integer(10)).eval())
        assertEquals(RealBigDec("8769956796.0826994768"), Power(N(Pi(),Integer(20)), Integer(20)).eval())
        assertEquals(RealBigDec("3.40282366920938463463374607431768211E+38"), Power(RealBigDec("2.00000000000000000000000000000000000"), Integer(128)).eval())
    }

    @Test
    fun realToRational() {
        assertEquals(RealDouble(2.23606797749979), Power(RealDouble(5.0), Rational(1,2)).eval())
        assertEquals(RealDouble(2040886.0816112224), Power(RealDouble(1.2345), Rational(12345,179)).eval())
        assertEquals(RealDouble(4.89983252377579E-7), Power(RealDouble(1.2345), Rational(-12345,179)).eval())

        // TODO_LP This invovles the Nth root so technically there are N answers and only 1 real, mathematica seems to give complex answer most of the time not sure what to do here yet
        //assertEquals(RealNum.create(1.0), Power(RealNum.create(-1.2345), Rational(3,2)).eval())

        assertEquals(RealBigDec("2"), Power(RealBigDec("4.0"), Rational.HALF).eval())
        assertEquals(RealBigDec("1.00006170"), Power(RealBigDec("1.0001234"), Rational(1,2)).eval()) // square root

        assertEquals(RealBigDec("1.587"), Power(RealBigDec("4.000"), Rational(1,3)).eval())
        assertEquals(RealBigDec("0.00117"), Power(RealBigDec("0.0001234"), Rational(3,4)).eval())
        assertEquals(RealBigDec("854.0"), Power(RealBigDec("0.0001234"), Rational(-3,4)).eval())
    }

    @Test
    fun realToReal() {
        // Double/Double
        assertEquals(RealDouble(55.90169943749474), Power(RealDouble(5.0), RealDouble(2.5)).eval())
        assertEquals(RealDouble(125.0), Power(RealDouble(5.0), RealBigDec("3.0")).eval())

        // Double/BigDecimal
        assertEquals(RealDouble(36.33783888017471), Power(RealBigDec("3.14"), RealDouble(3.14)).eval())
        assertEquals(RealDouble(36.33783888017471), Power(RealDouble(3.14), RealBigDec("3.14")).eval())

        // BigDec/BigDec
        assertEquals(RealBigDec("36.45"), Power(RealBigDec("3.1415"), RealBigDec("3.1415")).eval())
        assertEquals(RealBigDec("0.02743"), Power(RealBigDec("3.1415"), RealBigDec("-3.1415")).eval())
    }

    @Test
    fun realToComplex() {
        val piBigDec = RealBigDec("3.14")

        // Zero imaginary
        assertEquals(RealDouble(100.0), Power(RealDouble(10.0), Complex(2,0)).eval())
        assertEquals(RealBigDec("9.86"), Power(piBigDec, Complex(2,0)).eval())

        // Double
        assertEquals(Complex(24.70195964872899, 3.848790655850832), Power(RealDouble(5.0), Complex(2,4)).eval())
        assertEquals(Complex(1.5502967700299068, 1.6113906803859945), Power(RealDouble(5.0), Complex(Rational.HALF,Rational.HALF)).eval())
        assertEquals(Complex(24.70195964872899, 3.848790655850832), Power(RealDouble(5.0), Complex(2.0,4.0)).eval())
        assertEquals(Complex(52.40487058561866, -147.56137608427574), Power(RealDouble(5.0), Complex(piBigDec,piBigDec)).eval())
    }

    @Test
    fun realBigDecToComplex() {
        val piBigDec = RealBigDec("3.1416")
        val bigDec = RealBigDec("5.0001")
        assertEquals(Complex(RealBigDec("24.702"), RealBigDec("3.8547")), Power(bigDec, Complex(2,4)).eval())
        assertEquals(Complex(RealBigDec("1.5503"), RealBigDec("1.6115")), Power(bigDec, Complex(Rational.HALF,Rational.HALF)).eval())
        assertEquals(Complex(24.701991088784933, 3.855079473838341), Power(bigDec, Complex(2.0,4.0)).eval())
        // TODO_LP not quite right there should be 5 digits
        assertEquals(Complex(RealBigDec("52.95"), RealBigDec("-147.8")), Power(bigDec, Complex(piBigDec,piBigDec)).eval())
    }

    @Test
    fun complexToRational() {
        // Int/Rational: No precision to evaluate
        assertEquals(Power(Complex(2,4), Rational.HALF), Power(Complex(2,4), Rational.HALF).eval())
        assertEquals(Power(Complex(2,4), Rational(16,3)), Power(Complex(2,4), Rational(16,3)).eval())
        assertEquals(Power(Complex(Rational(3,2),Rational.HALF), Rational.HALF), Power(Complex(Rational(3,2),Rational.HALF), Rational.HALF).eval())

        // Can evaluate
        assertEquals(Complex(1.7989074399478673,1.1117859405028423), Power(Complex(RealDouble(2.0), Integer(4)), Rational.HALF).eval())
        assertEquals(Complex(0.07690324994251796,-0.18717392051825588), Power(Complex(RealDouble(2.0), Integer(4)), Rational(-16,15)).eval())

        assertEquals(Complex("1.536621", "0.5943192"), Power(Complex(RealBigDec("2.000001"), Integer(4)), Rational.THIRD).eval())
    }

    @Test
    fun complexToReal() {
        assertEquals(Complex(1.309544770737814, 6.174162506105573), Power(Complex(2.0,4.0), RealDouble(1.23)).eval())
        assertEquals(Complex(0.03287406851910734, -0.1549926705899962), Power(Complex(2,4), RealDouble(-1.23)).eval())

        assertEquals(Complex(1.309544770737814, 6.174162506105573), Power(Complex(RealDouble(2.0), Integer(4)), RealBigDec("1.23")).eval())
        assertEquals(Complex("1.3", "6.2"), Power(Complex(2,4), RealBigDec("1.23")).eval())
    }

    @Test
    fun complexToComplex() {
        assertEquals(Complex(0.2739572538301212, 0.5837007587586147), Power(Complex(1.0, 1.0), Complex(1.0,1.0)).eval())
        // Add more tests when BigDec can work, Int/Rational cannot be evaluated and RealDouble is simple to not need much testing

        // Cannot eval
        assertEquals(Power(Complex(1, 1), Complex(1,1)), Power(Complex(1, 1), Complex(1,1)).eval())
    }
}