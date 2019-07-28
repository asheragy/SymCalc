package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.*
import org.junit.Test

class FibonacciTest {

    @Test
    fun basic() {
        // TODO zero and negatives
        assertEquals(IntegerNum(1), Fibonacci(IntegerNum(1)).eval())
        assertEquals(IntegerNum(1), Fibonacci(IntegerNum(2)).eval())
        assertEquals(IntegerNum(2), Fibonacci(IntegerNum(3)).eval())
        assertEquals(IntegerNum(3), Fibonacci(IntegerNum(4)).eval())
        assertEquals(IntegerNum(5), Fibonacci(IntegerNum(5)).eval())
        assertEquals(IntegerNum(8), Fibonacci(IntegerNum(6)).eval())

        assertEquals(IntegerNum(6765), Fibonacci(IntegerNum(20)).eval())
        assertEquals(IntegerNum(12586269025), Fibonacci(IntegerNum(50)).eval())

        assertEquals("43466557686937456435688527675040625802564660517371780402481729089536555417949051890403879840079255169295922593080322634775209689623239873322471161642996440906533187938298969649928516003704476137795166849228875",
                Fibonacci(IntegerNum(1000)).eval().toString())
    }
}