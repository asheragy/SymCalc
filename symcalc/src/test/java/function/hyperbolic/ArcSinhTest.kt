package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.junit.Test

internal class ArcSinhTest {

    @Test
    fun bigDecimal() {
        ArcSinh("0.00") `==` "0.0"
        ArcSinh("1.000000000") `==` "0.8813735870"
        ArcSinh("2.1555") `==` "1.5111"
        ArcSinh("3.1415") `==` "1.8623"
        ArcSinh("3.1415926535897932384626433832795028841971693993751") `==` "1.8622957433108482198883613251826205749026741849616"
        ArcSinh("5.4321") `==` "2.3938"
        ArcSinh("-5.4321") `==` "-2.3938"
        ArcSinh("9.999999999") `==` "2.998222950"
        ArcSinh("100000000.0") `==` "19.11382792"
    }
}