package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.junit.Test

class ArithmeticTest {

    // TODO reflection to see if anything got added here

    @Test
    fun infinity() {
        val x = Infinity()

        Plus(x, x) `==` Infinity()
        Plus(x, 5) `==` Infinity()
        Plus(5, x) `==` Infinity()

        Subtract(x, x) `==` Indeterminate()
        Subtract(x, 5) `==` Infinity()
        Subtract(5, x) `==` Minus(Infinity())

        Log(x) `==` Infinity()
        // TODO add others
    }
}