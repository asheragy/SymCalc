package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.number.Integer
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

    @Test
    fun listResult() {
        assertAll(
                //Binomial(0) `==` ListExpr(1),
                //Binomial(1) `==` ListExpr(1, 1),
                Binomial(2) `==` ListExpr(1, 2, 1),
                Binomial(5) `==` ListExpr(1, 5, 10, 10, 5, 1),
                Binomial(6) `==` ListExpr(1, 6, 15, 20, 15, 6, 1),
        )
    }
}