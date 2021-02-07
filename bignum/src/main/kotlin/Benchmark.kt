package org.cerion.math.bignum

import java.math.BigInteger

const val multiplier = 0.1

fun main() {

    benchmark("Addition", addition())
    benchmark("Addition Big+Small", additionBigSmall())
    benchmark("Addition Small+Small", additionSmallSmall())

    benchmark("Multiply", multiply())
    benchmark("Multiply Big*Small", multiplyBigSmall())
}

private fun benchmark(name: String, result: Pair<Long, Long>) {
    val bigint = result.first.toString().padEnd(6)
    val java = result.second.toString().padEnd(6)
    val diff = result.first.toDouble() / result.second
    println("${name.padEnd(20)}\t$bigint\t$java\t$diff")
}

private fun addition(): Pair<Long, Long> {
    val str = "123456789098764321".repeat(550) // Result is about 1000 int32 digits
    val times = 900000.toMultiplier()
    val a = run {
        val a = BigInt(str)
        var x = BigInt("0")
        repeat(times) {
            x = x.add(a)
        }
    }

    val b = run {
        val a = BigInteger(str)
        var x = BigInteger("0")
        repeat(times) {
            x = x.add(a)
        }
    }

    return Pair(a, b)
}

private fun additionBigSmall(): Pair<Long, Long> {

    val str1 = "123456789098764321".repeat(550) // Result is about 1000 int32 digits
    val str2 = "1234567890"
    val times = 3000000.toMultiplier()
    val r1 = run {
        var a = BigInt(str1)
        val x = BigInt(str2)
        repeat(times) {
            a += x
        }
    }

    val r2 = run {
        var a = BigInteger(str1)
        val x = BigInteger(str2)
        repeat(times) {
            a += x
        }
    }

    return Pair(r1, r2)
}

private fun additionSmallSmall(): Pair<Long, Long> {
    val times = 50000000.toMultiplier()
    val r1 = run {
        val a = BigInt("1")
        var x = BigInt("0")
        repeat(times) {
            x += a
        }
    }

    val r2 = run {
        val a = BigInteger("1")
        var x = BigInteger("0")
        repeat(times) {
            x += a
        }
    }

    return Pair(r1, r2)
}

private fun multiply(): Pair<Long, Long> {
    val str = "123456789098764321".repeat(40) // Slightly under karatsuba threshold
    val times = 60000.toMultiplier()
    val r1 = run {
        val a = BigInt(str)
        val b = a + BigInt.ONE
        repeat(times) { a * b }
    }

    val r2 = run {
        val a = BigInteger(str)
        val b = a + BigInteger.ONE
        repeat(times) { a * b }
    }

    return Pair(r1, r2)
}

private fun multiplyBigSmall(): Pair<Long, Long> {
    val str = "123456789098764321".repeat(40) // Slightly under karatsuba threshold
    val times = 5000000.toMultiplier()
    val r1 = run {
        val a = BigInt(str)
        val b = BigInt(100000000)
        repeat(times) { a * b }
    }

    val r2 = run {
        val a = BigInteger(str)
        val b = BigInteger("100000000")
        repeat(times) { a * b }
    }

    return Pair(r1, r2)
}

private fun run(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    return end - start
}

private fun Int.toMultiplier() = (this * multiplier).toInt()
