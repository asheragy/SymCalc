package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.NumberType

class Fibonacci(vararg e: Expr) : FunctionExpr(*e) {

    // TODO zero/negatives and non-integer

    override fun evaluate(): Expr {
        val n = get(0) as Integer

        //return fibRecursive(n)
        return fibIterative(n)
    }

    private fun fibIterative(n: Integer): Integer {
        var a: NumberExpr = Integer.ZERO
        var b: NumberExpr = Integer.ONE
        var fib: Integer = Integer.ZERO

        //first 2 values are 0 and 1 so don't start loop unless 2 or higher
        if (n.isOne)
            fib = Integer(1)

        for (i in 1 until n.intValue()) {
            fib = (a + b).asInteger()
            a = b
            b = fib
        }

        return fib
    }

    /*
    private fun fibRecursive(n: IntegerNum): IntegerNum {
        return fibRecursive(n, IntegerNum.ZERO, IntegerNum.ONE)
    }

    private tailrec fun fibRecursive(n: IntegerNum, first: IntegerNum, second: IntegerNum): IntegerNum {
        if (n.isZero)
            return first

        val a = Subtract(n, IntegerNum.ONE).eval().asInteger()
        val b = Plus(first,second).eval().asInteger()
        return fibRecursive(a, b, first)
    }
     */

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }
}


