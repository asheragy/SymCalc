package expression.function.trig

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.function.trig.ArcCos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ArcCosTest {

    @Test
    fun basic() {

    }

    @Test
    fun evalDouble() {

    }

    @Test
    fun evalBigDec() {
        ArcCos("0.12345").eval() `should equal` "1.44703"
        ArcCos("0.9876543210123456789").eval() `should equal` "0.157296952180625718"
    }
}