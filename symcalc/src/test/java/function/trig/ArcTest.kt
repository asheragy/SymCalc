package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.junit.Test

class ArcTest {

    @Test
    fun arcCot() {
        // TODO these are correct but issues simplifying
        //ArcCot(0) `==` Pi() / 2
        //ArcCot(1) `==` Pi() / 4

        ArcCot(0.0) `==` 1.5707963267948966
        ArcCot(1.0) `==` 0.7853981633974483
        ArcCot(2.0) `==` 0.4636476090008061
    }
}