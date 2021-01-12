package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import kotlin.math.exp
import kotlin.test.Test

internal class EulerPhiTest {

    //@Test
    fun temp() {
        for(i in 1 until 11) {
            val e = EulerPhi(i).eval()
            println("$i $e")
        }
    }

    @Test
    fun first10() {
        val input = listOf(1,2,3,4,5,6,7,8,9,10)
        val expected = listOf(1,1,2,2,4,2,6,4,6,4)
        val map = input.mapIndexed { index, n -> EulerPhi(n) `==` expected[index] }

        assertAll(*map.toTypedArray())
    }

}