package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RandomIntegerTest {

    @Test
    fun binary() {
        val e = RandomInteger()

        val count = intArrayOf(0, 0)

        for (i in 0..9999) {
            val n = e.eval() as IntegerNum
            val x = n.intValue()
            count[x]++
        }

        assertTrue("0 count = " + count[0], count[0] > 4800)
        assertTrue("1 count = " + count[1], count[1] > 4800)
    }

    @Test
    fun maxValue() {
        val e = RandomInteger(IntegerNum(10))
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
        val e = RandomInteger(ListExpr(IntegerNum(10), IntegerNum(20)))
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
        val e = RandomInteger(IntegerNum(10))

        val prev = IntegerNum(-99)
        for (i in 0..4) {
            val curr = e.eval() as IntegerNum
            if (!prev.equals(curr))
                return
        }

        // All numbers were the same
        assertFalse(true)
    }
}