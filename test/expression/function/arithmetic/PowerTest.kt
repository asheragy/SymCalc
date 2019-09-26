package expression.function.arithmetic

import expression.constant.I
import expression.function.logical.Equal
import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

infix fun Expr.pow(other: Expr) = Power(this, other).eval()

class PowerTest {

    @Test
    fun toPowerZero() {
        val zeros = arrayOf(Integer.ZERO, Rational.ZERO, RealDouble(0.0), RealBigDec(BigDecimal.ZERO), Complex(0,0))
        val nums = arrayOf(Integer(5), Rational(2,3), RealDouble(3.14), RealBigDec(BigDecimal.TEN), Complex(7,9))

        for(num in nums) {
            for (zero in zeros) {
                val e = Power(num, zero).eval() as NumberExpr
                assertTrue(e.isOne, "Power($num,$zero)")
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
        val approx = Complex(-1.0, 1.2246467991473532E-16)
        assertEquals(approx, Power(N(E()), Times(N(Pi()), I())).eval())
        assertEquals(approx, Power(N(E()), Times(Pi(), I())).eval())
        assertEquals(approx, Power(E(), Times(N(Pi()), I())).eval())
    }

    @Test
    fun ePiI() {
        E() pow Pi() * I() `should equal` -1
        E() pow I() * Pi() `should equal` -1

        // Variations
        E() pow Integer(5) + Times(I(), Pi()) `should equal` Times(-1, Power(E(), 5))
        E() pow Times(2, I(), Pi()) `should equal` 1
        E() pow Times(I(), Divide(Pi(), 2)) `should equal` Complex(0,1)
        E() pow Times(I(), Times(Pi(), Rational(3,2))) `should equal` Complex(0,-1)
    }

    @Test
    fun toConstant() {
        assertEquals(Power(Integer(3), Pi()), Power(Integer(3), Pi()).eval())
        assertEquals(Power(Rational(2,3), Pi()), Power(Rational(2,3), Pi()).eval())
        assertEquals(RealDouble(31.54428070019754), Power(RealDouble(3.0), Pi()).eval())
        assertEquals(RealBigDec("36.459"), Power(RealBigDec("3.1415"), Pi()).eval())
        assertEquals(Power(Complex(3,3), Pi()), Power(Complex(3,3), Pi()).eval())
    }

    @Test
    fun fromConstant() {
        assertEquals(Power(Pi(), Integer(3)), Power(Pi(), Integer(3)).eval())
        assertEquals(Power(Pi(), Rational(2,3)), Power(Pi(), Rational(2,3)).eval())
        assertEquals(RealDouble(31.006276680299816), Power(Pi(), RealDouble(3.0)).eval())
        assertEquals(RealBigDec("36.458"), Power(Pi(), RealBigDec("3.1415")).eval())
        assertEquals(Power(Pi(), Complex(3,3)), Power(Pi(), Complex(3,3)).eval())
    }

    @Test
    fun numberExpr_pow() {
        // More tests in corresponding NumberExpr class, only 1 basic here per type to verify Power() calls it

        // Int
        Power(3, 2).eval() `should equal` 9
        Power(16807, Rational(1,5)).eval() `should equal` 7
        Power(3, 2.5).eval() `should equal` 15.588457268119896

        // Rational
        Power(Rational.HALF, 3).eval() `should equal` Rational(1,8)
        Power(Rational.HALF, 3.2).eval() `should equal` 0.1088188204120155

        // Double
        Power(5.0, 4).eval() `should equal` 625.0
        Power(5.0, Rational(1,2)).eval() `should equal` 2.23606797749979
        Power(5.0, 2.5).eval() `should equal` 55.90169943749474
        Power(5.0, "3.0").eval() `should equal` 125.0

        // BigDec
        Power("1.0001234", 2).eval() `should equal` "1.0002468"
        Power("4.0", Rational.HALF).eval() `should equal` "2"
        Power("3.14", 3.14).eval() `should equal` 36.33783888017471
        Power("3.000001", "2.35340583128859694839201928385968473749596868726265").eval() `should equal` "13.26967"

        // Complex to Int
        Power(Complex(3, Rational.HALF), 5).eval() `should equal` Complex(Rational(2823, 16), Rational(6121, 32))
        Power(Complex(0, 2.3), 5).eval() `should equal` Complex(0.0, 64.36342999999998)

        // Complex with any real number
        Power(Complex(2.0, 4), Rational(-16,15)).eval() `should equal` Complex(0.07690324994251796,-0.18717392051825588)
        Power(Complex(2,4), -1.23).eval() `should equal` Complex(0.03287406851910734, -0.1549926705899962)
        Power(Complex(2,4), "1.23").eval() `should equal` Complex("1.31", "6.17")
    }

    @Test
    fun integerToComplex() {
        // imaginary zero
        assertEquals(Integer(32), Power(Integer(2), Complex(5,0)).eval())

        // Cannot fully evaluate
        assertEquals(Power(Integer(2), Complex(2,3)), Power(Integer(2), Complex(2,3)).eval())

        assertEquals(Complex(-0.48699441796578125, 0.8734050817748715), N(Power(Integer(2), Complex(0,3))).eval())
        assertEquals(Complex(-1.947977671863125, 3.493620327099486), N(Power(Integer(2), Complex(2,3))).eval())
        assertEquals(Complex(-1.947977671863125, 3.493620327099486), Power(Integer(2), Complex(2.0, 3.0)).eval())
    }

    @Test
    fun rationalToRational() {
        assertEquals(Power(Integer.TWO, Rational.HALF.unaryMinus()), Power(Rational(1,2), Rational(1,2)).eval())
        assertEquals(Rational(4,9), Power(Rational(8,27), Rational(2,3)).eval())
        assertEquals(Integer(2), Power(Rational(1,4), Rational(-1,2)).eval())
        assertEquals(Rational.HALF, Power(Rational(1,4), Rational(1,2)).eval())
        assertEquals(Power(Integer.TWO, Rational.HALF), Power(Rational.HALF, Rational.HALF.unaryMinus()).eval())

        // Negative
        assertEquals(Times(Complex(0, 1), Power(Integer.TWO, Rational(-1,2))), Power(Rational.HALF.unaryMinus(), Rational.HALF).eval())
        assertEquals(Times(Integer.NEGATIVE_ONE, Power(Integer.TWO, Rational(-1,3))), Power(Rational.HALF.unaryMinus(), Rational.THIRD).eval())
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
        assertEquals(Complex("24.703", "3.8509"), Power(bigDec, Complex(2,4)).eval())
        assertEquals(Complex("1.5503", "1.6114"), Power(bigDec, Complex(Rational.HALF,Rational.HALF)).eval())
        assertEquals(Complex(24.70263974545859, 3.8509208127549734), Power(bigDec, Complex(2.0,4.0)).eval())
        assertEquals(Complex("52.933", "-147.81"), Power(bigDec, Complex(piBigDec,piBigDec)).eval())
    }

    @Test
    fun complexToRational() {
        // Int/Rational: No precision to evaluate
        assertEquals(Power(Complex(2,4), Rational.HALF), Power(Complex(2,4), Rational.HALF).eval())
        assertEquals(Power(Complex(2,4), Rational(16,3)), Power(Complex(2,4), Rational(16,3)).eval())
        assertEquals(Power(Complex(Rational(3,2),Rational.HALF), Rational.HALF), Power(Complex(Rational(3,2),Rational.HALF), Rational.HALF).eval())
    }

    @Test
    fun complexToComplex() {
        assertEquals(Complex(0.2739572538301212, 0.5837007587586147), Power(Complex(1.0, 1.0), Complex(1.0,1.0)).eval())
        // Add more tests when BigDec can work, Int/Rational cannot be evaluated and RealDouble is simple to not need much testing

        // Cannot eval
        assertEquals(Power(Complex(1, 1), Complex(1,1)), Power(Complex(1, 1), Complex(1,1)).eval())
    }
}