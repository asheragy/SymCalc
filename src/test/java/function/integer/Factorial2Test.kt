package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.number.Integer
import org.junit.Test

internal class Factorial2Test {

    @Test
    fun integers() {
        val expected = arrayOf(1, 1, 1, 2, 3, 8, 15, 48, 105, 384, 945, 3840)
        for(i in expected.indices)
            Factorial2(i-1).eval() `should equal` Integer(expected[i])
    }
}