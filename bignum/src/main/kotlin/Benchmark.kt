package org.cerion.math.bignum

import java.lang.management.ManagementFactory
import java.math.BigInteger

const val multiplier = 10.0

fun main() {
    var version = ManagementFactory.getRuntimeMXBean().getVmVersion();
    println("${"Name".padEnd(20)}\tMag\t\tBigInt\tJava\tDiff")


    benchmark("Divide Big/Single", "n", divideBigSingle(100000))
    benchmark("Divide", "n^2", divide(1000))


    //benchmark("Basic Ops", basicOps(500))
}

@Deprecated("use other")
private fun benchmark(name: String, result: Pair<Long, Long>) {
    return benchmark(name, "??", result)
}

private fun benchmark(name: String, mag: String, result: Pair<Long, Long>) {
    val bigint = result.first.toString().padEnd(6)
    val java = result.second.toString().padEnd(6)
    val diff = result.first.toDouble() / result.second
    println("${name.padEnd(20)}\tO($mag)\t$bigint\t$java\t$diff")
}



private fun divideBigSingle(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(50)

    val a = BigInt(str); val b = BigInt(100000000)
    val r1 = run(times) { a / b }

    val c = BigInteger(str); val d = BigInteger("100000000")
    val r2 = run(times) { c / d }

    return Pair(r1, r2)
}

private fun divide(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(50)
    val str_divisor = "123456789098764321".repeat(5)

    val a = BigInt(str); val b = BigInt(str_divisor)
    val r1 = run(times) { a / b }

    val c = BigInteger(str); val d = BigInteger(str_divisor)
    val r2 = run(times) { c / d }

    return Pair(r1, r2)
}


private fun run(times: Int, block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    repeat((times * multiplier).toInt()) {
        block()
    }
    val end = System.currentTimeMillis()
    return end - start
}
