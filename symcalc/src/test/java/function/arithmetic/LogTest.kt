package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.number.*
import org.cerion.symcalc.`should equal`
import kotlin.test.Test
import kotlin.test.assertEquals

class LogTest {

    @Test
    fun basic() {
        Log(2.0).eval() `should equal` 0.6931471805599453
        Log(0.0001).eval() `should equal` -9.210340371976182
        Log(ListExpr(2.0, 0.0001)).eval() `should equal` ListExpr(0.6931471805599453, -9.210340371976182)
    }

    @Test
    fun rational() {
        // Greater than 1
        assertEquals(Log(Rational(7,6)), Log(Rational(7,6)).eval())

        // Less than 1
        assertEquals(Times(Integer(-1), Log(Rational(7,6))), Log(Rational(6,7)).eval())
    }

    @Test
    fun zero() {
        // TODO need directed infinity
        //assertEquals(Integer.ZERO, Log(Integer.ZERO).eval())
    }

    @Test
    fun negative() {
        assertEquals(Plus(Times(Complex.I, Pi()), Log(Integer(10))), Log(Integer(-10)).eval())
        assertEquals(Plus(Times(Complex.I, Pi()), Log(Rational(4,3))), Log(Rational(-4, 3)).eval())
        assertEquals(Plus(Times(Complex.I, Pi()), Times(Integer(-1), Log(Integer(3)))), Log(Rational(-1, 3)).eval())

        assertEquals(Complex(-1.3862943611198906, 3.141592653589793), Log(RealDouble(-0.25)).eval())
        assertEquals(Complex(2.322387720290225, 3.141592653589793), Log(RealDouble(-10.2)).eval())
        assertEquals(Complex("1.63383", "3.14159"), Log(RealBigDec("-5.12345")).eval())
    }

    @Test
    fun e() {
        assertEquals(Integer.ONE, Log(E()).eval())
        assertEquals(Integer(7), Log(Power(E(),Integer(7))).eval())
    }

    @Test
    fun bigDecimal() {
        assertAll(
                Log("2.0") `==` "0.69",
                Log("2.000001") `==` "0.6931477",
                Log("10.000001") `==` "2.3025852",
                Log("100.000001") `==` "4.60517020")
    }

    //@Test
    fun temp() {
        val x100 = Rational(1001,10).eval(100)
        val x250 = Rational(1001,10).eval(250)
        Log(x100).eval() `should equal` RealBigDec("4.606169686321174901202792308289263426662958039650712504063675468764045544312363680026290664941397509")
        Log(x250).eval() `should equal` RealBigDec("4.606169686321174901202792308289263426662958039650712504063675468764045544312363680026290664941397508835067828144465612432101950443177354495663241233838524903260256246936957243232486023299562416895905726941502965091993506116907385338734535572300155929")

        for(i in 0 until 1000)
            Log(x100).eval()

        for(i in 0 until 270)
            Log(x250).eval()
    }

    @Test
    fun complex() {
        Log(ComplexInfinity()) `==` Infinity()
        Log(Complex(0,1)) `==` Complex(0, Rational.HALF) * Pi()
        Log(Complex(1,0)) `==` 0
        Log(Complex(1.0,1.0)) `==` Complex(0.3465735902799727, 0.7853981633974483)
    }
}