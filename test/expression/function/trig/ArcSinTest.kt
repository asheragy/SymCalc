package expression.function.trig

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.function.trig.ArcSin
import org.junit.jupiter.api.Test

internal class ArcSinTest {

    @Test
    fun evalBigDec() {
        ArcSin("0.50000").eval() `should equal` "0.52360"
        ArcSin("0.12345").eval() `should equal` "0.12377"
        //ArcSin("0.9876543210123456789").eval() `should equal` "1.41349937461427090"
    }

}