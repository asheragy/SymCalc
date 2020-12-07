package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals


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