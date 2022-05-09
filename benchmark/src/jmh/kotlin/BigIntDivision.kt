package org.cerion.math.bignum.benchmark

import org.cerion.math.bignum.BigInt2
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger

@ExperimentalUnsignedTypes
@State(Scope.Benchmark)
open class BigIntDivision {

    private val largeStr = "123456789098764321".repeat(40)
    private val largeDivisorStr = "123456789098764321".repeat(5)

    private val large = BigInt2(largeStr)
    private val largeJvm = BigInteger(largeStr)
    private val small = BigInt2(100000000)
    private val smallJvm = BigInteger.valueOf(100000000)
    private val largeDivisor = BigInt2(largeDivisorStr)
    private val largeDivisorJvm = BigInteger(largeDivisorStr)

    @Benchmark fun largeSmallJvm(bh: Blackhole) = bh.consume(largeJvm / smallJvm)
    @Benchmark fun largeSmall(bh: Blackhole) = bh.consume(large / small)

    @Benchmark fun largeJvm(bh: Blackhole) = bh.consume(largeJvm / largeDivisorJvm)
    @Benchmark fun large(bh: Blackhole) = bh.consume(large / largeDivisor)
}