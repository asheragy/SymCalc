package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.FunctionFactory
import org.cerion.symcalc.expression.number.*
import java.math.BigDecimal
import java.math.RoundingMode

class N(vararg e: Expr) : FunctionExpr(Function.N, *e) {

    constructor(e: Expr, precision: Int) : this(e) {
        if (precision > SYSTEM_DECIMAL_PRECISION)
            add(IntegerNum(precision))
    }

    override fun evaluate(): Expr {
        val e = args[0]
        val machinePrecision = size == 1
        val precision = if (machinePrecision) SYSTEM_DECIMAL_PRECISION else get(1).asInteger().intValue()

        if (e is ConstExpr) {
            return e.evaluate(precision)
        }

        if (e is RealNum_BigDecimal) {
            if (machinePrecision)
                return RealNum.create(e.asNumber().toDouble())

            if (precision < e.precision)
                return RealNum.create(e.value.setScale(precision, RoundingMode.HALF_UP))
        }

        if (e is RationalNum) {
            if (precision > 0) {
                val a = e.numerator.toBigDecimal()
                val b = e.denominator.toBigDecimal()
                val t = a.divide(b, precision, RoundingMode.HALF_UP)
                return RealNum.create(t)
            }

            return RealNum.create(e.numerator.toDouble() / e.denominator.toDouble())
        }

        val newArgs = mutableListOf<Expr>()
        for (i in 0 until e.args.size) {
            if (size > 1)
                newArgs.add(N(e.args[i], args[1]))
            else
                newArgs.add(N(e.args[i]))
        }

        if(e is FunctionExpr) {
            return FunctionFactory.createInstance(e.name, *newArgs.toTypedArray()).eval()
        }

        if (e is ConstExpr || e is NumberExpr) {
            if (size > 1 && get(1).isInteger) {
                val n = get(1) as IntegerNum
                e.setNumericalEval(true, n.intValue())
            } else
                e.isNumericalEval = true

            return e.eval()
        }

        /*
        if (size > 1 && get(1).isInteger) {
            val n = get(1) as IntegerNum
            args[0].setNumericalEval(true, n.intValue())
        } else
            args[0].isNumericalEval = true

        return get(0).eval()
        */
        return this

    }

    override fun validate() {
        validateParameterRage(1, 2)
    }
}
