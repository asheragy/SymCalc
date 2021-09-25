package org.cerion.math.bignum.benchmark

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger

@State(Scope.Benchmark)
internal open class BigIntBenchmark {

    @Benchmark
    fun test(bh: Blackhole) {

        bh.consume(BigInteger("100").pow(100))
    }
}