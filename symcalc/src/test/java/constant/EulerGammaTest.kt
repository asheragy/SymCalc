package org.cerion.symcalc.constant

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.junit.Test

internal class EulerGammaTest {

    private val smallValues = listOf(
            Pair(1, "0.6"),
            Pair(5, "0.57722"),
            Pair(10, "0.5772156649"),
            Pair(25, "0.5772156649015328606065121"),
            Pair(50, "0.57721566490153286060651209008240243104215933593992")
    )

    @Test
    fun smallValues() {
        val input = smallValues.map { EulerGamma().eval(it.first) `==` it.second }.toTypedArray()
        assertAll(*input)
    }

    @Test
    fun smallValues_computed() {
        val input = smallValues
                .take(3) // TODO remove this and still complete <100ms
                .map { EulerGamma().evalCompute(it.first) `==` it.second }.toTypedArray()
        assertAll(*input)
    }

    @Test
    fun largeNumber() {
        // These are too slow to run as regular unit tests when not using pre-computed digits
        assertAll(
                // TODO this should be at least 2x faster, check bernoulli comp which may be limiting factor OR algorithm that uses zeta function on integer input
                //EulerGamma().eval(100) `==` "0.5772156649015328606065120900824024310421593359399235988057672348848677267776646709369470632917467495"
                //EulerGamma().eval(201) `==` "0.577215664901532860606512090082402431042159335939923598805767234884867726777664670936947063291746749514631447249807082480960504014486542836224173997644923536253500333742937337737673942792595258247094916"
        )
    }
}