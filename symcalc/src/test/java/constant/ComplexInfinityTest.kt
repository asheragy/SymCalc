package org.cerion.symcalc.constant

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.number.Integer
import kotlin.test.Test

internal class ComplexInfinityTest {

    @Test
    fun arithmetic() {
        assertAll(
                Integer(5) + ComplexInfinity() `==` ComplexInfinity(),
                Integer(5) - ComplexInfinity() `==` ComplexInfinity(),
                Integer(5) * ComplexInfinity() `==` ComplexInfinity(),
                ComplexInfinity() / Integer(5) `==` ComplexInfinity())
    }

    // TODO add other operators lke power/log
}