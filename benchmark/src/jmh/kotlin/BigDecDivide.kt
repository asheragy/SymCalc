package org.cerion.math.bignum.benchmark

import org.cerion.math.bignum.decimal.BigDec
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@ExperimentalUnsignedTypes
@State(Scope.Benchmark)
open class BigDecDivide {

    private val numeratorJvm = BigDecimal("1.1").pow(100)
    private val numerator10 = BigDec("1.1").pow(100)
    private val denominatorJvm = BigDecimal("0.9").pow(100)
    private val denominator10 = BigDec("0.9").pow(100)

    private val mc = MathContext(80, RoundingMode.HALF_UP)

    @Benchmark fun basicJvm(bh: Blackhole) = bh.consume(numeratorJvm.divide(denominatorJvm, mc))
    @Benchmark fun basic10(bh: Blackhole) = bh.consume(numerator10.divide(denominator10, mc))
}