package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.`==`
import org.junit.Test

internal class ArcCoshTest {

    @Test
    fun bigDecimal() {
        ArcCosh("1.000000000") `==` "0.0"
        ArcCosh("2.1555") `==` "1.4024"
        ArcCosh("3.1415") `==` "1.8115"
        ArcCosh("3.1415926535897932384626433832795028841971693993751") `==` "1.8115262724608531070218520493054205102207020810579"
        ArcCosh("5.4321") `==` "2.3769"
        ArcCosh("9.999999999") `==` "2.993222846"
        ArcCosh("100000000.0") `==` "19.11382792"
    }

    @Test
    fun bigDecimal_lessThanOne() {
        // TODO these are complex results
    }
}