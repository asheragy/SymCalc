package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.number.Integer
import org.junit.Assert.assertEquals
import org.junit.Test

class PowerModTest {

    @Test
    fun basic() {
        val a = Integer("1234567890987654321")
        val b = Integer("1122334455667788990")
        val m = Integer("5555555555555555555")

        val e = PowerMod(a, b, m)
        assertEquals(Integer("498317946897227631"), e.eval())
    }
}