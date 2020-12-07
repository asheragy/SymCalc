package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.number.RealBigDec
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class ArcCoshTest {

    @Test
    fun bigDecimal() {
        assertAll(
                ArcCosh(RealBigDec("1.000000000")) `==` RealBigDec.ZERO,
                ArcCosh(RealBigDec("2.1555")) `==` RealBigDec("1.4024"),
                ArcCosh(RealBigDec("3.1415")) `==` RealBigDec("1.8115"),
                ArcCosh(RealBigDec("3.1415926535897932384626433832795028841971693993751")) `==` RealBigDec("1.8115262724608531070218520493054205102207020810579"),
                ArcCosh(RealBigDec("5.4321")) `==` RealBigDec("2.3769"),
                ArcCosh(RealBigDec("9.999999999")) `==` RealBigDec("2.993222846"),
                ArcCosh(RealBigDec("100000000.0")) `==` RealBigDec("19.11382792")
        )
    }

    @Test
    fun bigDecimal_lessThanOne() {
        // TODO these are complex results
    }
}