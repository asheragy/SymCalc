package org.cerion.symcalc.number.primitive

@ExperimentalUnsignedTypes
object BigIntArray {

    internal fun compare(x: UIntArray, y: UIntArray): Int {
        val size = x.size.compareTo(y.size)
        if (size != 0)
            return size

        // Start at most significant digit
        for (i in x.size - 1 downTo 0)
            if (x[i].compareTo(y[i]) != 0)
                return x[i].compareTo(y[i])

        return 0
    }

    internal fun add(x: UIntArray, y: UIntArray): UIntArray {
        val a = if (x.size >= y.size) x else y
        val b = if (x.size >= y.size) y else x

        val arr = UIntArray(a.size)
        var index = 0
        var sum: ULong = 0uL

        // Add digits while array lengths are the same
        while(index < b.size) {
            sum = a[index].toULong() + b[index].toULong() + (sum shr 32)
            arr[index++] = sum.toUInt()
        }

        // Add digits for larger number while digit is still carried
        var carry = sum shr 32 != 0uL
        while(index < a.size && carry) {
            sum = a[index].toULong() + (sum shr 32)
            carry = sum shr 32 != 0uL
            arr[index++] = sum.toUInt()
        }

        // If there is still a digit to carry add it and return
        if (carry) {
            val t = arr.copyOf(arr.size + 1)
            t[arr.size] = 1u
            return t
        }

        // If no more carry just add the remaining digits
        while(index < a.size)
            arr[index] = a[index++]

        return arr
    }

    internal fun subtract(x: UIntArray, y: UIntArray): UIntArray {
        // Input is always expected to have x as the largest array and adjust result sign accordingly
        val arr = UIntArray(x.size) // TODO is there a benefit in copying starting array? need to performance test first
        var index = 0
        var diff = 0L

        while(index < y.size) {
            diff = x[index].toLong() - y[index].toLong() + (diff shr 32)
            arr[index++] = diff.toUInt()
        }

        var borrow = diff < 0
        while(index < x.size && borrow) {
            diff = x[index].toLong() - 1
            borrow = diff < 0
            arr[index++] = diff.toUInt()
        }

        while(index < x.size)
            arr[index] = x[index++]

        return removeLeadingZeros(arr)
    }

    fun removeLeadingZeros(arr: UIntArray): UIntArray {
        var digits = 0
        for(i in arr.size -1 downTo 0) {
            if (arr[i] == 0u)
                digits++
            else
                break
        }

        if (digits > 0) // TODO, this drops elements from end of array is there a way to do that without copy?
            return arr.copyOf(arr.size - digits)

        return arr
    }
}