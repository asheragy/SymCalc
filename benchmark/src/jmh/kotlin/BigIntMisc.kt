package org.cerion.math.bignum.benchmark

import org.cerion.math.bignum.integer.BigInt10
import org.cerion.math.bignum.integer.BigInt2
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger

@ExperimentalUnsignedTypes
@State(Scope.Benchmark)
open class BigIntMisc {

    private val str = "123456789098764321".repeat(40) // TODO find what different algorithms are called for various sizes of this
    //private val str ="123456789098764321".repeat(1000) // Test this when larger multiplication is closer on speed
    private val large = BigInt2(str)
    private val largeJvm = BigInteger(str)
    private val large10 = BigInt10(str)

    @Benchmark fun constructJvm(bh: Blackhole) = bh.consume(BigInteger(str))
    @Benchmark fun construct(bh: Blackhole) = bh.consume(BigInt2(str))
    @Benchmark fun construct10(bh: Blackhole) = bh.consume(BigInt10(str))

    @Benchmark fun toStringJvm(bh: Blackhole) = bh.consume(largeJvm.toString())
    @Benchmark fun toString(bh: Blackhole) = bh.consume(large.toString())
    @Benchmark fun toString10(bh: Blackhole) = bh.consume(large10.toString())
}