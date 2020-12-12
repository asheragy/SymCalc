package org.cerion.symcalc.constant

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.function.core.N
import kotlin.test.Test

class ETest {
    @Test
    fun basic() {
        assertAll(
                E() `==` E(),
                N(E()) `==` 2.718281828459045)
    }

    private val smallValues = listOf(
            Pair(4, "2.718"),
            Pair(5, "2.7183"),
            Pair(20, "2.7182818284590452354"),
            Pair(50, "2.7182818284590452353602874713526624977572470937000"),
            Pair(100, "2.718281828459045235360287471352662497757247093699959574966967627724076630353547594571382178525166427"))

    @Test
    fun realBigDec() {
        val input = smallValues.map { E().eval(it.first) `==` it.second }.toTypedArray()
        assertAll(*input)
    }

    @Test
    fun realBigDec_compute() {
        val input = smallValues.map { E().evalCompute(it.first) `==` it.second }.toTypedArray()
        assertAll(*input)
    }
}