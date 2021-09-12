package org.cerion.math.bignum

import java.math.BigInteger

const val multiplier = 1.0

fun main() {
    println("${"Name".padEnd(20)}\tMag\t\tBigInt\tJava\tDiff")
    ///benchmark("Construct", construct(20))
    benchmark("tostring", string(50))

    //benchmark("Addition", addition(90000))
    //benchmark("Addition Big+Small", additionBigSmall(300000))
    //benchmark("Addition Small+Small", additionSmallSmall(5000000))

    //benchmark("Subtraction", subtraction(90000))

    benchmark("Multiply Big*Small", "n", multiplyBigSmall(1200000))
    benchmark("Multiply Big*Big", "n^2", multiply(50000))

    //benchmark("Basic Ops", basicOps(500))
}

@Deprecated("use other")
private fun benchmark(name: String, result: Pair<Long, Long>) {
    return benchmark(name, "", result)
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
    val str = "123456789098764321".repeat(1000)
    val a = BigInt(str)
    val r1 = run(times) { a.toString() }

    val b = BigInteger(str)
    val r2 = run(times) { b.toString() }
    return Pair(r1, r2)
}

private fun addition(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(550) // Result is about 1000 int32 digits
    val a1 = BigInt(str)
    var x1 = BigInt("0")
    val a = run(times) { x1 = x1.add(a1) }

    val a2 = BigInteger(str)
    var x2 = BigInteger("0")
    val b = run(times) { x2 = x2.add(a2) }

    return Pair(a, b)
}

private fun additionBigSmall(times: Int): Pair<Long, Long> {
    val str1 = "123456789098764321".repeat(550) // Result is about 1000 int32 digits
    val str2 = "1234567890"
    var a1 = BigInt(str1)
    val x1 = BigInt(str2)
    val r1 = run(times) { a1 += x1 }

    var a2 = BigInteger(str1)
    val x2 = BigInteger(str2)
    val r2 = run(times) { a2 += x2 }

    return Pair(r1, r2)
}

private fun additionSmallSmall(times: Int): Pair<Long, Long> {
    val a1 = BigInt("1"); var x1 = BigInt("0")
    val r1 = run(times) { x1 += a1 }

    val a2 = BigInteger("1"); var x2 = BigInteger("0")
    val r2 = run(times) { x2 += a2 }

    return Pair(r1, r2)
}


private fun subtraction(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(550) // Result is about 1000 int32 digits
    var a1 = BigInt(str)
    val subtract1 = a1 / BigInt("293840932483209")
    val a = run(times) { a1 -= subtract1  }

    var a2 = BigInteger(str)
    val subtract2 = a2 / BigInteger("293840932483209")
    val b = run(times) { a2 -= subtract2 }

    return Pair(a, b)
}

private fun multiply(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(40) // Slightly under karatsuba threshold
    val a1 = BigInt(str)
    val b1 = a1 + BigInt.ONE
    val r1 = run(times) { a1 * b1 }

    val a2 = BigInteger(str)
    val b2 = a2 + BigInteger.ONE
    val r2 = run(times) { a2 * b2 }

    return Pair(r1, r2)
}

private fun multiplyBigSmall(times: Int): Pair<Long, Long> {
    val str = "123456789098764321".repeat(50)

    val a = BigInt(str); val b = BigInt(100000000)
    val r1 = run(times) { a * b }

    val c = BigInteger(str); val d = BigInteger("100000000")
    val r2 = run(times) { c * d }

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
