package org.cerion.symcalc.expression

import kotlin.test.Test
import kotlin.test.assertNotEquals

class ErrorExprTest {

    @Test
    fun equals() {
        val e1 = ErrorExpr("error 1")
        val e2 = ErrorExpr("error 2")

        // Different
        assertNotEquals(e1, e2)
        assertNotEquals(e1, e1)
    }
}