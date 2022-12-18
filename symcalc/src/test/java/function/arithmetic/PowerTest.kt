package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.function.logical.Equal
import org.cerion.symcalc.number.*
import org.cerion.symcalc.`should equal`
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertTrue

infix fun Expr.pow(other: Expr) = Power(this, other).eval()

class PowerTest {

    @Test
    fun toStringTest() {
        Power("x").toString() `should equal` "x"
        Power("x", "y").toString() `should equal` "x^y"
        Power(2, 3).toString() `should equal` "2^3"
        Power(2, 3, 4).toString() `should equal` "2^3^4"
    }

    @Test
    fun singleParameter() {
        Power("x") `==` VarExpr("x")
        Power(2) `==` 2
    }

    @Test
    fun multiParameter() {
        Power("x", "y", "z") `==` Power("x", Power("y", "z"))
        Power(2, 3, 2) `==` 512
    }

    @Test
    fun toPowerZero() {
        val zeros = arrayOf(0, Rational.ZERO, 0.0, RealBigDec(BigDecimal.ZERO), Complex(0,0))
        val nums = arrayOf(5, Rational(2,3), 3.14, RealBigDec(BigDecimal.TEN), Complex(7,9))

        for(num in nums) {
            for (zero in zeros) {
                val e = Power(num, zero).eval() as NumberExpr
                assertTrue(e.isOne, "Power($num,$zero)")
            }
        }
    }

    @Test
    fun toPowerOne() {
        val ones = arrayOf(1, Rational.ONE, 1.0, RealBigDec(BigDecimal.ONE), Complex(1,0))
        val nums = arrayOf(5, Rational(2,3), 3.14, RealBigDec(BigDecimal.TEN), Complex(7,9))

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
        Power(N(E()), Times(N(Pi()), I())) `==` approx
        Power(N(E()), Times(Pi(), I())) `==` approx
        Power(E(), Times(N(Pi()), I())) `==` approx
    }

    @Test
    fun ePiI() {
        E() pow Pi() * I() `==` -1
        E() pow I() * Pi() `==` -1

        // Variations
        E() pow Integer(5) + Times(I(), Pi()) `==` Times(-1, Power(E(), 5))
        E() pow Times(2, I(), Pi()) `==` 1
        E() pow Times(I(), Divide(Pi(), 2)) `==` Complex(0,1)
        E() pow Times(I(), Times(Pi(), Rational(3,2))) `==` Complex(0,-1)
    }

    @Test
    fun toConstant() {
        Power(3, Pi()) `==` Power(3, Pi())
        Power(Rational(2,3), Pi()) `==` Power(Rational(2,3), Pi())
        Power(3.0, Pi()) `==` 31.54428070019754
        Power("3.1415", Pi()) `==` "36.459"
        Power(Complex(3,3), Pi()) `==` Power(Complex(3,3), Pi())
    }

    @Test
    fun fromConstant() {
        Power(Pi(), 3) `==` Power(Pi(), 3)
        Power(Pi(), Rational(2,3)) `==` Power(Pi(), Rational(2,3))
        Power(Pi(), 3.0) `==` 31.006276680299816
        Power(Pi(), "3.1415") `==` "36.458"
        Power(Pi(), Complex(3,3)) `==` Power(Pi(), Complex(3,3))
    }

    @Test
    fun numberExpr_pow() {
        // More tests in corresponding NumberExpr class, only 1 basic here per type to verify Power() calls it

        // Int
        Power(3, 2) `==` 9
        Power(16807, Rational(1,5)) `==` 7
        Power(3, 2.5) `==` 15.588457268119896

        // Rational
        Power(Rational.HALF, 3) `==` Rational(1,8)
        Power(Rational.HALF, 3.2) `==` 0.1088188204120155

        // Double
        Power(5.0, 4) `==` 625.0
        Power(5.0, Rational(1,2)) `==` 2.23606797749979
        Power(5.0, 2.5) `==` 55.90169943749474
        Power(5.0, "3.0") `==` 125.0

        // BigDec
        Power("1.0001234", 2) `==` "1.0002468"
        Power("4.0", Rational.HALF) `==` "2.0"
        Power("3.14", 3.14) `==` 36.33783888017471
        Power("3.000001", "2.35340583128859694839201928385968473749596868726265") `==` "13.26967"

        // Complex to Int
        Power(Complex(3, Rational.HALF), 5) `==` Complex(Rational(2823, 16), Rational(6121, 32))
        Power(Complex(0, 2.3), 5) `==` Complex(0.0, 64.36342999999998)

        // Complex with any real number
        Power(Complex(2.0, 4), Rational(-16,15)) `==` Complex(0.07690324994251796,-0.18717392051825588)
        Power(Complex(2,4), -1.23) `==` Complex(0.03287406851910734, -0.1549926705899962)
        Power(Complex(2,4), "1.23") `==` Complex("1.31", "6.17")
    }

    @Test
    fun integerToComplex() {
        // imaginary zero
        Power(2, Complex(5,0)) `==` 32

        // Cannot fully evaluate
        Power(2, Complex(2,3)) `==` Power(2, Complex(2,3))

        N(Power(2, Complex(0,3))) `==` Complex(-0.48699441796578125, 0.8734050817748715)
        N(Power(2, Complex(2,3))) `==` Complex(-1.947977671863125, 3.493620327099486)
        Power(2, Complex(2.0, 3.0)) `==` Complex(-1.947977671863125, 3.493620327099486)
    }

    @Test
    fun rationalToRational() {
        Power(Rational(1,2), Rational(1,2)) `==` Power(2, Rational.HALF.unaryMinus())
        Power(Rational(8,27), Rational(2,3)) `==` Rational(4,9)
        Power(Rational(1,4), Rational(-1,2)) `==` 2
        Power(Rational(1,4), Rational(1,2)) `==` Rational.HALF
        Power(Rational.HALF, Rational.HALF.unaryMinus()) `==` Power(2, Rational.HALF)

        // Negative
        Power(Rational.HALF.unaryMinus(), Rational.HALF) `==` Times(Complex(0, 1), Power(2, Rational(-1,2)))
        Power(Rational.HALF.unaryMinus(), Rational.THIRD) `==` Times(-1, Power(2, Rational(-1,3)))
    }

    @Test
    fun rationalToComplex() {
        // No imaginary
        Power(Rational.HALF, Complex(4,0)) `==` Rational(1,16)

        N(Power(Rational.HALF, Complex(4,2))) `==` Complex(0.011466060921456358, -0.061439233775702734)
        Power(Rational.HALF, Complex(-4.0,-2.0)) `==` Complex(2.9353115958928275, 15.7284438465799)

        // Cannot eval
        Power(Rational.HALF, Complex(4,2)) `==` Power(Rational.HALF, Complex(4,2))
    }

    @Test
    fun realToComplex() {
        // Zero imaginary
        Power(10.0, Complex(2,0)) `==` 100.0
        Power("3.14", Complex(2,0)) `==` "9.86"

        // Double
        Power(5.0, Complex(2,4)) `==` Complex(24.70195964872899, 3.848790655850832)
        Power(5.0, Complex(Rational.HALF,Rational.HALF)) `==` Complex(1.5502967700299068, 1.6113906803859945)
        Power(5.0, Complex(2.0,4.0)) `==` Complex(24.70195964872899, 3.848790655850832)
        Power(5.0, Complex("3.14","3.14")) `==` Complex(52.40487058561866, -147.56137608427574)
    }

    @Test
    fun realBigDecToComplex() {
        val piBigDec = RealBigDec("3.1416")
        val bigDec = RealBigDec("5.0001")

        Power(bigDec, Complex(2,4)) `==` Complex("24.703", "3.8509")
        Power(bigDec, Complex(Rational.HALF,Rational.HALF)) `==` Complex("1.5503", "1.6114")
        Power(bigDec, Complex(2.0,4.0)) `==` Complex(24.70263974545859, 3.8509208127549734)
        Power(bigDec, Complex(piBigDec,piBigDec)) `==` Complex("52.933", "-147.81")
    }

    @Test
    fun complexToRational() {
        // Int/Rational: No precision to evaluate
        Power(Complex(2,4), Rational.HALF) `==` Power(Complex(2,4), Rational.HALF)
        Power(Complex(2,4), Rational(16,3)) `==` Power(Complex(2,4), Rational(16,3))
        Power(Complex(Rational(3,2),Rational.HALF), Rational.HALF) `==` Power(Complex(Rational(3,2),Rational.HALF), Rational.HALF)
    }

    @Test
    fun complexToComplex() {
        Power(Complex(1.0, 1.0), Complex(1.0,1.0)) `==` Complex(0.2739572538301212, 0.5837007587586147)
        // Add more tests when BigDec can work, Int/Rational cannot be evaluated and RealDouble is simple to not need much testing

        // Cannot eval
        Power(Complex(1, 1), Complex(1,1)) `==` Power(Complex(1, 1), Complex(1,1))
    }

    @Test
    fun powToPow() {
        Power(Power(3, Rational.HALF), -1) `==` Power(3, Rational(-1, 2))
    }

    @Test
    fun multiplicationSpread() {
        val x = VarExpr("x")
        val y = VarExpr("y")
        Power(x * y, 2) `==` Times(Power(x, 2), Power(y, 2))

        // Should do nothing
        Power(x, 2) * Power(y, 2) `==` Times(Power(x, 2), Power(y, 2))
    }
}