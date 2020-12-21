package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test

class BinomialTest {

    @Test
    fun basic() {
        assertAll(
                Binomial(10, 3) `==` 120,
                Binomial(20, 7) `==` 77520,
                Binomial(50, 20) `==` Integer("47129212243960"),
                Binomial(100, 30) `==` Integer("29372339821610944823963760")
        )
    }
}