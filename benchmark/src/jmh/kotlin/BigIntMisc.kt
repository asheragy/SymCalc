package org.cerion.math.bignum.benchmark

import org.cerion.math.bignum.BigInt
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger

@ExperimentalUnsignedTypes
@State(Scope.Benchmark)
open class BigIntMisc {

    private val str ="123456789098764321".repeat(1000)
    private val large = BigInt(str)
    private val largeJvm = BigInteger(str)

    @Benchmark fun constructJvm(bh: Blackhole) = bh.consume(BigInteger(str))
    @Benchmark fun construct(bh: Blackhole) = bh.consume(BigInt(str))

    @Benchmark fun toStringJvm(bh: Blackhole) = bh.consume(largeJvm.toString())
    @Benchmark fun toString(bh: Blackhole) = bh.consume(large.toString())
}