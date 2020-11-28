package expression.function.trig

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.trig.ArcSin
import org.cerion.symcalc.expression.number.Rational
import org.junit.jupiter.api.Test

internal class ArcSinTest {

    @Test
    fun asInteger() {
        ArcSin(1).eval() `should equal` Rational(1,2) * Pi()
        ArcSin(0).eval() `should equal` 0
        ArcSin(-1).eval() `should equal` Rational(-1,2) * Pi()
    }

    @Test
    fun asDouble() {
        ArcSin(1.0).eval() `should equal` 1.5707963267948966
        ArcSin(0.0).eval() `should equal` 0.0
        ArcSin(-1.0).eval() `should equal` -1.5707963267948966
        ArcSin(5.0).eval() `should equal` Double.NaN
    }

    @Test
    fun bigDecBasic() {
        ArcSin("-1.0000").eval() `should equal` "-1.5708"
        ArcSin("-0.75000").eval() `should equal` "-0.84806"
        ArcSin("-0.50000").eval() `should equal` "-0.52360"
        ArcSin("-0.25000").eval() `should equal` "-0.25268"
        ArcSin("0.0000").eval() `should equal` "0.0000"
        ArcSin("0.25000").eval() `should equal` "0.25268"
        ArcSin("0.50000").eval() `should equal` "0.52360"
        ArcSin("0.75000").eval() `should equal` "0.84806"
        ArcSin("1.0000").eval() `should equal` "1.5708"
    }

    @Test
    fun bigDecLarge() {
        ArcSin("0.9876543210123456789").eval() `should equal` "1.413499374614270902"
    }
}