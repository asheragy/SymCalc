package org.cerion.math.bignum

@ExperimentalUnsignedTypes
class BigDec {

    val value: BigInt
    val scale: Int

    constructor(value: BigInt, scale: Int) {
        this.value = value
        this.scale = scale
    }

    constructor(str: String) {
        var index = str.indexOf(".")
        if (index == -1)
            scale = 0
        else
            scale = str.length - index - 1

        value = BigInt(str.replace(".", ""))
    }

}