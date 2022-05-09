package org.cerion.math.bignum

import org.cerion.math.bignum.integer.BigInt2
import java.math.BigDecimal


fun main() {

    val x = BigInt2("0001")

    val a  = BigDecimal("10000000000.0")
    val a1 = BigDecimal("10000000000")
    //val b = BigDecimal("100")
    val c = BigDecimal("0.000001")


    val d = a + c
    val e = a1 + c

    println("")
}