package expression.function.arithmetic

import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.function.arithmetic.Exp
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class ExpTest {

    @Test
    fun identity() {
        assertAll(
                { assertEquals(Integer(1), Exp(Integer(0)).eval()) },
                { assertEquals(E(), Exp(Integer(1)).eval()) }
        )
    }

    @Test
    fun basic() {
        assertAll(
                { assertEquals(Power(E(), Integer(5)), Exp(Integer(5)).eval()) },
                { assertEquals(Power(E(), Rational(1, 2)), Exp(Rational(1, 2)).eval()) },
                { assertEquals(RealDouble(20.085536923187664), Exp(RealDouble(3.0)).eval()) },
                { assertEquals(RealBigDec("23.1"), Exp(RealBigDec("3.140")).eval()) }
        )
    }
}