package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.number.*
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
        // TODO_LP
        // need directed infinity
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
        val x100 = Rational(11,10).eval(100)
        val x250 = Rational(11,10).eval(250)
        Log(x100).eval() `should equal` RealBigDec("0.09531017980432486004395212328076509222060536530864419918523980816300101423588423283905750291303649307")
        Log(x250).eval() `should equal` RealBigDec("0.09531017980432486004395212328076509222060536530864419918523980816300101423588423283905750291303649307274794184585174988884604369351298063868901502170232637556873469835512041574566077311170504814066115849672190926276831999726668041246291711632113962014")

        for(i in 0 until 700)
            Log(x100).eval()

        for(i in 0 until 120)
            Log(x250).eval()
    }

    @Test
    fun complex() {
        // FEAT
    }
}