package expression.number

import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Test
import kotlin.test.assertFailsWith

class NumberExprTest {

    @Test
    fun unsupportedOperations() {
        val integer = IntegerNum.ONE
        val real = RealNum.create(0.0)
        val rational = RationalNum(1,2)
        val complex = ComplexNum(1,1)

        assertFailsWith<UnsupportedOperationException> { integer.power(rational) }
        assertFailsWith<UnsupportedOperationException> { integer.power(complex) }
    }
}