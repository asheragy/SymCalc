package expression.function.arithmetic

import expression.constant.I
import expression.function.logical.Equal
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.*
import org.junit.Assert.*
import org.junit.Test

class PowerTest {

    // More specific cases are handled in NumberExpr classes as Power() is just calling those

    @Test
    fun zero() {
        val zeros = arrayOf(IntegerNum.ZERO, RationalNum.ZERO, RealNum.create(0.0), ComplexNum(0,0))
        val nums = arrayOf(IntegerNum(5), RationalNum(2,3), RealNum.create(3.14), ComplexNum(7,9))

        for(num in nums) {
            for (zero in zeros) {
                assertTrue("Power($num,$zero)", Power(num, zero).eval().asNumber().isOne)
            }
        }
    }

    @Test
    fun identity() {
        val ones = arrayOf(IntegerNum.ONE, RationalNum.ONE, RealNum.create(1.0), ComplexNum(1,0))
        val nums = arrayOf(IntegerNum(5), RationalNum(2,3), RealNum.create(3.14), ComplexNum(7,9))

        for(num in nums) {
            for (one in ones) {
                val power = Power(num, one).eval()
                assertTrue("Power($num,$one)", Equal(num, power).eval().asBool().value())
            }
        }
    }

    @Test
    fun ePiI() {
        val approx = ComplexNum(RealNum.create(-1.0), RealNum.create(1.2246467991473532E-16))
        assertEquals(approx, Power(N(E()), Times(N(Pi()), I())).eval())
        assertEquals(approx, Power(N(E()), Times(Pi(), I())).eval())
        assertEquals(approx, Power(E(), Times(N(Pi()), I())).eval())

        assertEquals(IntegerNum(-1), Power(E(), Times(Pi(), I())).eval())
        assertEquals(IntegerNum(-1), Power(E(), Times(I(), Pi())).eval())

        // Variations
        assertEquals(Times(IntegerNum(-1),Power(E(), IntegerNum(5))), Power(E(), Plus(IntegerNum(5), Times(I(), Pi()))).eval())
        assertEquals(IntegerNum.ONE, Power(E(), Times(IntegerNum.TWO, I(), Pi())).eval())
        assertEquals(ComplexNum(0,1), Power(E(), Times(I(), Divide(Pi(), IntegerNum.TWO))).eval())
    }
}