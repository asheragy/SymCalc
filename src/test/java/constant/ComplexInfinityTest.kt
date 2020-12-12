package org.cerion.symcalc.constant

import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.function.arithmetic.Divide
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

internal class ComplexInfinityTest {

    @Test
    fun arithmetic() {
        assertEquals(ComplexInfinity(), Integer(5) + ComplexInfinity())
        assertEquals(ComplexInfinity(), Integer(5) - ComplexInfinity())
        assertEquals(ComplexInfinity(), Integer(5) * ComplexInfinity())
        assertEquals(ComplexInfinity(), Divide(ComplexInfinity(), Integer(5)).eval())
    }

    // TODO_LP add other operators lke power/log
}