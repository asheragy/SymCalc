package expression.function.trig

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.function.trig.ArcSin
import org.junit.jupiter.api.Test

internal class ArcSinTest {

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