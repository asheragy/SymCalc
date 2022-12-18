package org.cerion.symcalc.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.Rational

class ArcSec(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        TODO("Not yet implemented")
    }
}

class ArcCsc(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        TODO("Not yet implemented")
    }
}

class ArcCot(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val z = get(0)

        if (z is NumberExpr) {
            val i = Complex(0, 1)
            return i * Rational.HALF * (Log(z - i) - Log(z + i))
        }

        return this
    }
}

