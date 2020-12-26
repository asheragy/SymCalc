package org.cerion.symcalc.function.integer

import org.cerion.symcalc.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals

class FibonacciTest {

    @Test
    fun basic() {
        assertEquals(Integer(1), Fibonacci(Integer(1)).eval())
        assertEquals(Integer(1), Fibonacci(Integer(2)).eval())
        assertEquals(Integer(2), Fibonacci(Integer(3)).eval())
        assertEquals(Integer(3), Fibonacci(Integer(4)).eval())
        assertEquals(Integer(5), Fibonacci(Integer(5)).eval())
        assertEquals(Integer(8), Fibonacci(Integer(6)).eval())

        assertEquals(Integer(6765), Fibonacci(Integer(20)).eval())
        assertEquals(Integer(12586269025), Fibonacci(Integer(50)).eval())

        assertEquals("43466557686937456435688527675040625802564660517371780402481729089536555417949051890403879840079255169295922593080322634775209689623239873322471161642996440906533187938298969649928516003704476137795166849228875",
                Fibonacci(Integer(1000)).eval().toString())
    }
}