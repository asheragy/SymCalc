package org.cerion.symcalc.function.statistics

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RandomIntegerTest {

    @Test
    fun binary() {
        val e = RandomInteger()

        val count = intArrayOf(0, 0)

        for (i in 0..9999) {
            val n = e.eval() as Integer
            val x = n.intValue()
            count[x]++
        }

        assertTrue(count[0] > 4800, "0 count = " + count[0])
        assertTrue(count[1] > 4800, "1 count = " + count[1])
    }

    @Test
    fun maxValue() {
        val e = RandomInteger(Integer(10))
        val found = intArrayOf(0, 5, 10)

        for (i in 0..9999) {
            val n = e.eval().asInteger()
            val rand = n.intValue()
            if (rand == 0)
                found[0] = -1
            else if (rand == 5)
                found[1] = -1
            else if (rand == 10)
                found[2] = -1

            if (found[0] == -1 && found[1] == -1 && found[2] == -1)
                return
        }

        assertFalse(true)
    }

    @Test
    fun range() {
        val e = RandomInteger(ListExpr(Integer(10), Integer(20)))
        val found = intArrayOf(10, 15, 20)

        for (i in 0..9999) {
            val n = e.eval().asInteger()
            val rand = n.intValue()
            if (rand == 10)
                found[0] = -1
            else if (rand == 15)
                found[1] = -1
            else if (rand == 20)
                found[2] = -1
            else if (rand < 10)
                assertFalse(true)
            else if (rand > 20)
                assertFalse(true)

            if (found[0] == -1 && found[1] == -1 && found[2] == -1)
                return
        }

        assertFalse(true)
    }

    @Test
    fun numbersAreRandom() {
        val e = RandomInteger(Integer(10))

        val prev = Integer(-99)
        for (i in 0..4) {
            val curr = e.eval() as Integer
            if (!prev.equals(curr))
                return
        }

        // All numbers were the same
        assertFalse(true)
    }
}