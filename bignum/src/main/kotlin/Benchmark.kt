package org.cerion.math.bignum

import java.lang.management.ManagementFactory
import java.math.BigInteger

const val multiplier = 10.0

fun main() {
    var version = ManagementFactory.getRuntimeMXBean().getVmVersion();
    println("${"Name".padEnd(20)}\tMag\t\tBigInt\tJava\tDiff")
    /*
    benchmark("Construct", construct(20))
    benchmark("toString", "n^2", string(100))

    benchmark("Subtraction", "n", subtraction(90000))

    benchmark("Multiply Big*Single", "n", multiplyBigSingle(1200000))
    benchmark("Multiply Small", "n^2", multiplySmall(3500000))
    benchmark("Multiply Medium", "n^2", multiplyMedium(4000))


    benchmark("Divide Big/Single", "n", divideBigSingle(100000))
    benchmark("Divide", "n^2", divide(1000))
     */


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

private fun construct(times: Int): Pair<Long, Long> {
    val str ="123456789098764321".repeat(1000)
    val r1 = run(times) { BigInt(str) }
    val r2 = run(times) { BigInteger(str) }
    return Pair(r1, r2)
}

private fun string(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(500)
    val a = BigInt(str)
    val r1 = run(times) { a.toString() }

    val b = BigInteger(str)
    val r2 = run(times) { b.toString() }
    return Pair(r1, r2)
}


private fun multiplySmall(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(2)
    val a1 = BigInt(str)
    val b1 = a1 + BigInt.ONE
    val r1 = run(times) { a1 * b1 }

    val a2 = BigInteger(str)
    val b2 = a2 + BigInteger.ONE
    val r2 = run(times) { a2 * b2 }

    return Pair(r1, r2)
}

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

private fun multiplyBigSingle(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(50)

    val a = BigInt(str); val b = BigInt(100000000)
    val r1 = run(times) { a * b }

    val c = BigInteger(str); val d = BigInteger("100000000")
    val r2 = run(times) { c * d }

    return Pair(r1, r2)
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

private fun basicOps(times: Int): Pair<Long, Long> {
    val a1 = BigInt("9".repeat(5000))
    val b1 = BigInt("7".repeat(5000))
    val c1 = BigInt("3".repeat(9999))
    val d1 = BigInt("1".repeat(9980))
    val r1 = run(times) {
        var x = a1 + a1
        x *= b1
        x -= c1
        x.divide(d1)
    }

    val a2 = BigInteger("9".repeat(5000))
    val b2 = BigInteger("7".repeat(5000))
    val c2 = BigInteger("3".repeat(9999))
    val d2 = BigInteger("1".repeat(9980))
    val r2 = run(times) {
        var x = a2 + a2
        x *= b2
        x -= c2
        x.divide(d2)
    }

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
