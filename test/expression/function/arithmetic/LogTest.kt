package expression.function.arithmetic

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Log
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LogTest {

    @Test
    fun basic() {
        assertEquals(RealDouble(0.6931471805599453), Log(RealDouble(2.0)).eval())
        assertEquals(RealDouble(-9.210340371976182), Log(RealDouble(0.0001)).eval())
        assertEquals(ListExpr(RealDouble(0.6931471805599453), RealDouble(-9.210340371976182)), Log(ListExpr(RealDouble(2.0), RealDouble(0.0001))).eval())
    }

    @Test
    fun rational() {
        // TODO simplified to -1 * Log
    }

    @Test
    fun zero() {
        // TODO
        // need directed infinity
        //assertEquals(Integer.ZERO, Log(Integer.ZERO).eval())
    }

    @Test
    fun negative() {
        assertEquals(Plus(Times(Complex.I, Pi()), Log(Integer(10))), Log(Integer(-10)).eval())
        assertEquals(Plus(Times(Complex.I, Pi()), Log(Rational(4,3))), Log(Rational(-4, 3)).eval())
        assertEquals(Plus(Times(Complex.I, Pi()), Times(Integer(-1), Log(Rational(1,3)))), Log(Rational(-1, 3)).eval())

        assertEquals(Complex(RealDouble(-1.3862943611198906), RealDouble(3.141592653589793)), Log(RealDouble(-0.25)).eval())
        assertEquals(Complex(RealDouble(2.322387720290225), RealDouble(3.141592653589793)), Log(RealDouble(-10.2)).eval())
        assertEquals(Complex(RealBigDec("1.63383"), RealBigDec("3.14159")), Log(RealBigDec("-5.12345")).eval())
    }

    @Test
    fun e() {
        assertEquals(Integer.ONE, Log(E()).eval())
        // TODO add variations
    }

    @Test
    fun bigDecimal() {
        assertEquals(RealBigDec("0.7"), Log(RealBigDec("2.0")).eval())
        assertEquals(RealBigDec("0.693148"),   Log(RealBigDec("2.000001")).eval())
        assertEquals(RealBigDec("2.3025852"),  Log(RealBigDec("10.000001")).eval())
        assertEquals(RealBigDec("4.60517020"), Log(RealBigDec("100.000001")).eval())
    }

    @Test
    fun complex() {
        // TODO_LP
    }
}