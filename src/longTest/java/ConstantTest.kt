package org.cerion.symcalc

import org.cerion.symcalc.constant.Pi
import kotlin.test.Test

class ConstantTest {

    @Test
    fun piLarge() {
        Pi().evalCompute(8200)
    }

    @Test
    fun piMultiple() {
        for(i in 0 until 45000)
            Pi().evalCompute(100)
    }
}