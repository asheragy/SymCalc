package expression.function.logical

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert
import org.junit.Test

class EqualTest {

    @Test
    fun numbers() {
        val numbers = arrayOf(IntegerNum(2), RationalNum(4,2), RealNum.create(2.0), ComplexNum(2,0))

        for(n1 in numbers) {
            for (n2 in numbers)
                isEqual(n1, n2)
        }
    }

    private fun isEqual(a: Expr, b: Expr) {
        val equal = Equal(a, b).eval()
        Assert.assertTrue("$a == $b", equal.asBool().value())
    }
}