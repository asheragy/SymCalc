package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.FunctionFactory
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealBigDec

class N(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = args[0]
        val machinePrecision = size == 1
        val precision = if (machinePrecision) MachinePrecision else get(1).asInteger().intValue()

        if (precision == 0)
            return RealBigDec.ZERO

        if (e is ConstExpr)
            return e.evaluate(precision)
        if (e is NumberExpr)
            return e.evaluate(precision)

        val newArgs = mutableListOf<Expr>()
        for (i in 0 until e.args.size) {
            newArgs.add(e.args[i].eval(precision))
        }

        if(e is FunctionExpr) {
            return FunctionFactory.createInstance(e.name, *newArgs.toTypedArray()).eval()
        }

        return this
    }

    override fun validate() {
        validateParameterRage(1, 2)
    }
}
