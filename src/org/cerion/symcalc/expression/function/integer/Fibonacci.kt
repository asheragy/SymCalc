package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum

class Fibonacci(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.FIBONACCI, *e) {

    override fun evaluate(): Expr {
        val n = (get(0) as IntegerNum).intValue()

        var a: NumberExpr = IntegerNum.ZERO
        var b: NumberExpr = IntegerNum.ONE
        var fib: NumberExpr = IntegerNum.ZERO
        if (n == 1)
        //first 2 values are 0 and 1 so don't start loop unless 2 or higher
            fib = IntegerNum(1)

        // TODO overload IntegerNum so while loop works
        for (i in 1 until n) {
            fib = a.add(b)
            a = b
            b = fib
        }

        return fib
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberExpr.INTEGER)
    }
}


