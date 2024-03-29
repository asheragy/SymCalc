package org.cerion.symcalc.function.special

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import org.junit.Assert
import org.junit.Test
import kotlin.test.Ignore

internal class ZetaTest {

    @Test
    fun trivialZeros() = assertAll(
        Zeta(Integer(-2)) `==` Integer.ZERO,
        Zeta(Integer(-4)) `==` Integer.ZERO,
        Zeta(Integer(-10)) `==` Integer.ZERO,
        Zeta(Integer(-12345678)) `==` Integer.ZERO,
        Zeta(Integer("-10000000000000000006")) `==` Integer.ZERO,
        // Non-Integer
        Zeta(RealDouble(-2.0)) `==` 0.0,
        Zeta(RealDouble(-12345678.0)) `==` 0.0,
        Zeta(RealBigDec("-2.0000000000")) `==` RealBigDec.ZERO,
        //Zeta(RealBigDec("-4978848521029382.0000000000")) `==` RealBigDec.ZERO)
    )

    @Test
    fun integer_positiveEven() = assertAll(
        Zeta(Integer(0)) `==` Rational.HALF.unaryMinus(),
        Zeta(Integer(2)) `==` Times(Rational(1,6), Power(Pi(), Integer(2))),
        Zeta(Integer(4)) `==` Times(Rational(1,90), Power(Pi(), Integer(4))),
        Zeta(Integer(6)) `==` Times(Rational(1,945), Power(Pi(), Integer(6))),
        Zeta(Integer(12)) `==` Times(Rational(691,638512875), Power(Pi(), Integer(12))),
    )

    @Test
    fun integer_positiveOdd() {
        assertAll(
                Zeta(Integer(1)) `==` ComplexInfinity(),
                Zeta(Integer(3)) `==` Zeta(Integer(3)),
                Zeta(Integer(99)) `==` Zeta(Integer(99)))
    }

    @Test
    fun integer_negativeOdd() {
        assertAll(
                Zeta(Integer(-1)) `==` Rational(-1, 12),
                Zeta(Integer(-3)) `==` Rational(1, 120),
                Zeta(Integer(-5)) `==` Rational(-1, 252),
                Zeta(Integer(-7)) `==` Rational(1, 240),
                Zeta(Integer(-13)) `==` Rational(-1, 12),
                Zeta(Integer(-15)) `==` Rational(3617, 8160),
                Zeta(Integer(-25)) `==` Rational(-657931, 12),
                Zeta(Integer(-27)) `==` Rational(Integer("3392780147"), Integer("3480")))
    }

    @Test
    fun realBigDec_oddInteger() {
        assertAll(
                Zeta(Integer(3).toPrecision(100)) `==` "1.202056903159594285399738161511449990764986292340498881792271555341838205786313090186455873609335258",
                Zeta(Integer(5).toPrecision(100)) `==` "1.036927755143369926331365486457034168057080919501912811974192677903803589786281484560043106557133336",
                Zeta(Integer(7).toPrecision(100)) `==` "1.008349277381922826839797549849796759599863560565238706417283136571601478317355735346096968913851324",
                Zeta(Integer(13).toPrecision(100)) `==` "1.000122713347578489146751836526357395714275105895509845136702671620896726829844209812892713953268136",
                Zeta(Integer(15).toPrecision(100)) `==` "1.000030588236307020493551728510645062587627948706858177506569932893332267156342279573072334347017548",
                Zeta(Integer(97).toPrecision(100)) `==` "1.000000000000000000000000000006310887241768094495682609394333203750069471198356939003268601281544896",
                Zeta(Integer(99).toPrecision(100)) `==` "1.000000000000000000000000000001577721810442023616644432783015960178223709238624152775914274573141831"
        )
    }

    @Test
    fun realBigDec_evenInteger() {
        assertAll(
                Zeta(Integer(2).toPrecision(10)) `==` "1.644934067",
                Zeta(Integer(2).toPrecision(100)) `==` "1.644934066848226436472415166646025189218949901206798437735558229370007470403200873833628900619758705",
                Zeta(Integer(8).toPrecision(10)) `==` "1.004077356",
                Zeta(Integer(30).toPrecision(10)) `==` "1.000000001",
        )
    }

    //@Test
    fun realBigDec() {
        assertAll(
                Zeta(RealBigDec("3.14")) `==` "1.18",
                Zeta(RealBigDec("5.555555555")) `==` "1.024170497"
        )
    }

    // ---------------- Performance -----------------
    // All run in about ~1000ms

    @Ignore
    @Test
    fun zetaLarge() {
        val x = RealBigDec("3.14", 530)
        run(Zeta(x))
    }

    @Ignore
    @Test
    fun zetaMultiple() {
        val x = RealBigDec("3.14", 70)
        for(i in 0 until 90)
            if(Zeta(x).eval() is ErrorExpr) // TODO add wrapper for everything that checks result
                throw RuntimeException()
    }

    @Ignore
    @Test
    fun zetaOddInteger_multiple() {
        repeat(7) {
            for (i in 3 until 100) {
                run(Zeta(Integer(i).toPrecision(100)))
            }
        }
    }

    @Ignore
    @Test
    fun zetaOddInteger_Large() {
        for (i in 3 until 100) {
            run(Zeta(Integer(i).toPrecision(450)))
        }
    }

    private fun run(expr: Expr) {
        val res = expr.eval()
        if (res is ErrorExpr)
            Assert.fail(res.toString())
    }
}