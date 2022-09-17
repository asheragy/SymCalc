package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals


class FactorTest {
    @Test
    fun basic() {
        // 2*2
        assertEquals(ListExpr(Integer.TWO, Integer.TWO), Factor(Integer(4)).eval())

        // 2*2*2
        assertEquals(ListExpr(Integer.TWO, Integer.TWO, Integer.TWO), Factor(Integer(8)).eval())

        // 2*2*3
        assertEquals(ListExpr(Integer.TWO, Integer.TWO, Integer(3)), Factor(Integer(12)).eval())

        // 2*2*3*3
        //(ListExpr(Integer.TWO, Integer.TWO, Integer(3), Integer(3)), Factor(Integer(36)).eval())

        // 5*5
        assertEquals(ListExpr(Integer(5), Integer(5)), Factor(Integer(25)).eval())

        // 5*5*7
        assertEquals(ListExpr(Integer(7), Integer(5), Integer(5)), Factor(Integer(175)).eval())

        // 5*5*7*7
        assertEquals(ListExpr(Integer(7), Integer(7), Integer(5), Integer(5)), Factor(Integer(1225)).eval())
    }

    @Test
    fun largerNumbers() {
        // 137 * 727 * 3803
        assertEquals(ListExpr(Integer(137), Integer(727), Integer(3803)), Factor(Integer(378774997)).eval())
    }

    @Test
    fun test() {
        val primes = listOf(48611,611953,7368787,86028121,982451653,11037271757).map { Integer(it) }
        val a = primes[0]
        val b = primes[1]
        assertEquals(ListExpr(a, b), Factor(a * b).eval())
    }
}