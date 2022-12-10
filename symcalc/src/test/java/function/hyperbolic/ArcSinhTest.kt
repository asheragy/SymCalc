package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.number.RealBigDec
import org.junit.Test

internal class ArcSinhTest {

    @Test
    fun bigDecimal() {
        assertAll(
                ArcSinh(RealBigDec("0.00")) `==` RealBigDec("0.0"),
                ArcSinh(RealBigDec("1.000000000")) `==` RealBigDec("0.8813735870"),
                ArcSinh(RealBigDec("2.1555")) `==` RealBigDec("1.5111"),
                ArcSinh(RealBigDec("3.1415")) `==` RealBigDec("1.8623"),
                ArcSinh(RealBigDec("3.1415926535897932384626433832795028841971693993751")) `==` RealBigDec("1.8622957433108482198883613251826205749026741849616"),
                ArcSinh(RealBigDec("5.4321")) `==` RealBigDec("2.3938"),
                ArcSinh(RealBigDec("-5.4321")) `==` RealBigDec("-2.3938"),
                ArcSinh(RealBigDec("9.999999999")) `==` RealBigDec("2.998222950"),
                ArcSinh(RealBigDec("100000000.0")) `==` RealBigDec("19.11382792")
        )
    }
}