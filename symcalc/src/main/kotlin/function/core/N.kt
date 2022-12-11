package org.cerion.symcalc.function.core

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.FunctionFactory
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.RealBigDec

class N(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = args[0]
        val machinePrecision = size == 1
        val precision = if (machinePrecision) MachinePrecision else get(1).asInteger().intValue()

        if (precision == 0)
            return RealBigDec.ZERO

        if (e is VarExpr)
            return e
        if (e is ConstExpr)
            return e.evaluate(precision)
        if (e is NumberExpr)
            return e.toPrecision(precision)

        if(e is FunctionExpr) {
            val newArgs = mutableListOf<Expr>()
            for (i in 0 until e.args.size) {
                newArgs.add(e.args[i].eval(precision))
            }
            return FunctionFactory.createInstance(e.name, *newArgs.toTypedArray()).eval()
        }

        return this
    }

    override fun validate() {
        validateParameterRange(1, 2)
    }
}
