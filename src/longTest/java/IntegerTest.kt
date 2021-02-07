package org.cerion.symcalc

import org.cerion.symcalc.function.integer.Bernoulli
import org.cerion.symcalc.function.integer.Binomial
import kotlin.test.Test

class IntegerTest {

    @Test
    // ~1.5s
    fun binomial() {
        val a = Binomial(500)
        repeat(500) {
            a.eval()
        }
    }

    @Test
    // ~1.2
    fun bernoulli() {
        val a = Bernoulli(100)
        repeat(200) {
            a.eval()
        }
    }
}