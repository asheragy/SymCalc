package expression.constant

import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.number.Integer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ComplexInfinityTest {

    @Test
    fun arithmetic() {
        assertEquals(ComplexInfinity(), Plus(Integer(5), ComplexInfinity()).eval())
        assertEquals(ComplexInfinity(), Subtract(Integer(5), ComplexInfinity()).eval())
        assertEquals(ComplexInfinity(), Times(Integer(5), ComplexInfinity()).eval())
        assertEquals(ComplexInfinity(), Divide(ComplexInfinity(), Integer(5)).eval())
    }

    // TODO_LP add other operators lke power/log
}