package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberType

class Factor(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        var num = get(0) as Integer

        val listArgs = mutableListOf<Expr>()
        while (num.isEven) {
            num = (num / Integer.TWO).asInteger()
            listArgs.add(Integer.TWO)
        }

        while(num != Integer.ONE) {
            val d = pollardRho(num)
            if (d == num)
                break

            listArgs.add(d)
            num = (num / d).asInteger()
        }

        if (num != Integer.ONE)
            listArgs.add(num)

        return ListExpr(listArgs)
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }
}

fun pollardRho(n: Integer): Integer {
    if (n.value.isProbablePrime(5))
        return n

    var seed = Integer.ONE

    while(true) {
        val result = pollardRhoAttempt(n, seed)
        if (result != null)
            return result

        seed++
    }
}

fun pollardRhoAttempt(n: Integer, seed: Integer): Integer? {
    var x = Integer(2)
    var y = Integer(2)
    var d = Integer.ONE

    val g = { x: Integer ->
        Mod(x.square().plus(seed),n).eval() as Integer
    }

    while(d == Integer.ONE) {
        x = g(x)
        y = g(g(y))
        d = x.minus(y).abs().gcd(n)
    }

    if(d == n)
        return null

    return d
}