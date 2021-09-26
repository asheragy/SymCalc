package org.cerion.math.bignum.benchmark

import org.cerion.math.bignum.BigInt
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger

@ExperimentalUnsignedTypes
@State(Scope.Benchmark)
open class BigIntAdd {

    private val largeIntStr = "123456789098764321".repeat(550) // Result is about 1000 int32 digits
    private val largeIntJvm = BigInteger(largeIntStr)
    private val largeInt =  BigInt(largeIntStr)
    private val smallIntJvm = BigInteger("1234567890")
    private val smallInt = BigInt("1234567890")
    private val subtractJvm = largeIntJvm / BigInteger("293840932483209")
    private val subtract = largeInt / BigInt("293840932483209")

    @Benchmark fun largeJvm(bh: Blackhole) = bh.consume(largeIntJvm + largeIntJvm)
    @Benchmark fun large(bh: Blackhole) = bh.consume(largeInt + largeInt)

    @Benchmark fun smallJvm(bh: Blackhole) = bh.consume(smallIntJvm + smallIntJvm)
    @Benchmark fun small(bh: Blackhole) = bh.consume(smallInt + smallInt)

    @Benchmark fun largeSmallJvm(bh: Blackhole) = bh.consume(largeIntJvm + smallIntJvm)
    @Benchmark fun largeSmall(bh: Blackhole) = bh.consume(largeInt + smallInt)

    @Benchmark fun subtractJvm(bh: Blackhole) = bh.consume(largeIntJvm - subtractJvm)
    @Benchmark fun subtract(bh: Blackhole) = bh.consume(largeInt - subtract)
}

