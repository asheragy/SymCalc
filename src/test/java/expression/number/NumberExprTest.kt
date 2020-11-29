package org.cerion.symcalc.expression.number

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class NumberExprTest : NumberTestBase() {

    @Test
    fun equals_afterEval() {
        assertNotEquals(Integer.ONE as NumberExpr, Complex(1, 0) as NumberExpr)
        assertEquals(Integer.ONE, Complex(1, 0).eval())

        assertNotEquals(Integer.ONE as NumberExpr, Rational(1,1) as NumberExpr)
        assertEquals(Integer.ONE, Rational(1,1).eval())
    }

    @Test
    fun compareToPrecision() {
        val bigHalf = Rational.HALF.toPrecision(500)
        assertEquals(0, bigHalf.compareTo(Rational.HALF))
        assertEquals(0, Rational.HALF.compareTo(bigHalf))

        val almostHalf = RealBigDec("0.50000000000000000000000000000000000000000000000000001")
        assertEquals(1, almostHalf.compareTo(Rational.HALF))
        assertEquals(-1, Rational.HALF.compareTo(almostHalf))
    }
}