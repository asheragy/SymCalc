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
open class BigIntMultiply {

    val multiplyToLenMaxStr = "123456789098764321".repeat(40) // Slightly under karatsuba threshold uses multiplyToLen()
    private val a_multiplyLenJvm = BigInteger(multiplyToLenMaxStr)
    private val b_multiplyLenJvm = a_multiplyLenJvm.add(BigInteger.ONE)
    private val a_multiplyLen = BigInt2(multiplyToLenMaxStr)
    private val b_multiplyLen = a_multiplyLen.add(BigInt2.ONE)
    private val a_multiplyLen10 = BigInt10(multiplyToLenMaxStr)
    private val b_multiplyLen10 = a_multiplyLen10.add(BigInt10.ONE)

    private val large = BigInt2("123456789098764321").pow(565) as BigInt2 // About 1000 digits
    private val large2 = large.add(BigInt2.ONE)
    private val largeJvm = BigInteger("123456789098764321").pow(565)
    private val large2Jvm = largeJvm.add(BigInteger.ONE)
    private val small = BigInt2(100000000)
    private val smallJvm = BigInteger.valueOf(100000000)

    // List of 40 sizes all under karatsuba threshold
    private val strs = MutableList(40) {n -> "123456789098764321".repeat(n+1) }
    private val range = strs.map { BigInt2(it) }
    private val rangeJvm = strs.map { BigInteger(it) }

    @Benchmark fun multiplyToLenJvm(bh: Blackhole) = bh.consume(a_multiplyLenJvm * b_multiplyLenJvm)
    @Benchmark fun multiplyToLen(bh: Blackhole) = bh.consume(a_multiplyLen * b_multiplyLen)
    @Benchmark fun multiplyToLen10(bh: Blackhole) = bh.consume(a_multiplyLen10 * b_multiplyLen10)

    @Benchmark fun largeJvm(bh: Blackhole) = bh.consume(largeJvm * large2Jvm)
    @Benchmark fun large(bh: Blackhole) = bh.consume(large * large2)

    @Benchmark fun largeSmallJvm(bh: Blackhole) = bh.consume(largeJvm * smallJvm)
    @Benchmark fun largeSmall(bh: Blackhole) = bh.consume(large * small)

    @Benchmark fun rangeJvm(bh: Blackhole) {
        bh.consume(range.mapIndexed { index, bigInt ->
            if (index < range.size - 1)
                bigInt * range[index+1]
        })
    }
    @Benchmark fun range(bh: Blackhole) {
        bh.consume(rangeJvm.mapIndexed { index, bigInt ->
            if (index < range.size - 1)
                bigInt * rangeJvm[index + 1]
        })
    }
}

/*

// Multiply arrays of various sizes under basic multiplication threshold
private fun multiplyMedium(times: Int): Pair<Long, Long> {
    val strs = MutableList(40) {n -> "123456789098764321".repeat(n+1) }
    val a = strs.map { BigInt(it) }
    val r1 = run(times) {
        for(i in 0 until strs.size - 1)
            a[i] * a[i+1]
    }

    val b = strs.map { BigInteger(it) }
    val r2 = run(times) {
        for (i in 0 until strs.size - 1)
            b[i] * b[i + 1]
    }

    return Pair(r1, r2)
}


 */