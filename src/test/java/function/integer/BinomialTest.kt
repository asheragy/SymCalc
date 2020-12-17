package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test

class BinomialTest {

    @Test
    fun basic() {
        assertAll(
                Binomial(Integer(10), Integer(3)) `==` Integer(120),
                Binomial(Integer(20), Integer(7)) `==` Integer(77520),
                Binomial(Integer(50), Integer(20)) `==` Integer("47129212243960"),
                Binomial(Integer(100), Integer(30)) `==` Integer("29372339821610944823963760")
        )
    }
}