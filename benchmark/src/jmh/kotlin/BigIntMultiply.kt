package org.cerion.math.bignum.benchmark

import org.cerion.math.bignum.BigInt
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger


@ExperimentalUnsignedTypes
@State(Scope.Benchmark)
open class BigIntMultiply {

    private lateinit var a_jvmMultiplyLen: BigInteger
    private lateinit var b_jvmMultiplyLen: BigInteger
    private lateinit var a_multiplyLen: BigInt
    private lateinit var b_multiplyLen: BigInt

    @Setup
    fun setup() {
        val str = "123456789098764321".repeat(40) // Slightly under karatsuba threshold
        a_multiplyLen = BigInt(str)
        b_multiplyLen = a_multiplyLen.add(BigInt.ONE)

        a_jvmMultiplyLen = BigInteger(str)
        b_jvmMultiplyLen = BigInteger(str)
    }

    @Benchmark
    fun multiplyToLen_jvm(bh: Blackhole) {
        bh.consume(a_jvmMultiplyLen * b_jvmMultiplyLen)
    }

    @Benchmark
    fun multiplyToLen(bh: Blackhole) {
        bh.consume(a_multiplyLen * b_multiplyLen)
    }
}