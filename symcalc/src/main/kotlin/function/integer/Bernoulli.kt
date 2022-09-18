package org.cerion.symcalc.function.integer

import org.cerion.math.bignum.integer.BinomialGenerator
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.NumberType
import org.cerion.symcalc.number.Rational

class Bernoulli(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        if (e is Integer) {
            val intVal = e.intValue()
            if (intVal > 2 && intVal % 2 == 1)
                return Integer.ZERO

            // TODO https://www.williamstein.org/edu/2006/spring/583/notes/2006-04-14/2006-04-14.pdf

            return calculate(intVal).last()
        }

        return this
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }

    companion object {
        fun list(n: Int): List<NumberExpr> = calculate(n)
    }
}

/*
 * Generate Bernoulli numbers from 0 to MAX
 */
private fun calculate(max: Int): List<NumberExpr> {
    val Bn = Array<NumberExpr>(max+1) { Integer.ZERO}
    val binomial = BinomialGenerator(max)
    binomial.inc()
    Bn[0] = Integer.ONE

    if(max > 0) {
        Bn[1] = Rational.HALF.unaryMinus()
        binomial.inc()
    }

    for(n in 2..max) {
        binomial.inc()
        if (n % 2 == 1)
            continue

        var sum: NumberExpr = Integer.ZERO
        for(k in 0 until n)
            sum += (Integer(binomial[k]) * Bn[k])

        val Bi = Integer.NEGATIVE_ONE.div(Integer(n + 1))
        Bn[n] = Bi * sum
    }

    return Bn.toList()
}