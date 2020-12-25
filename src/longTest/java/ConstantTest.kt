package org.cerion.symcalc

import org.cerion.symcalc.constant.Pi
import kotlin.test.Test

class ConstantTest {

    @Test
    fun piLarge() {
        Pi().evalCompute(7000)
    }

    @Test
    fun piMultiple() {
        for(i in 0 until 16000)
            Pi().evalCompute(100)
    }
}