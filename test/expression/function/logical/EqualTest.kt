package expression.function.logical

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.Assert
import org.junit.Test

class EqualTest {

    @Test
    fun numbers() {
        val numbers = arrayOf(IntegerNum(2), Rational(4,2), RealNum_Double(2.0), Complex(2,0))

        for(n1 in numbers) {
            for (n2 in numbers)
                isEqual(n1, n2)
        }
    }

    @Test
    fun precision_evaluatedFirst() {
        isEqual(Pi(), N(Pi()))
    }

    private fun isEqual(a: Expr, b: Expr) {
        val equal = Equal(a, b).eval()
        Assert.assertTrue("$a == $b", equal.asBool().value)
    }
}