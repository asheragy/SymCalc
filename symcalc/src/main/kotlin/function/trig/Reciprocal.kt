package org.cerion.symcalc.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Divide

class Sec(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val cos = Cos(get(0)).eval()

        if (cos is Cos)
            return this

        return Divide(1, cos).eval()
    }
}

class Csc(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val sin = Sin(get(0)).eval()
        if (sin is Sin)
            return this

        return Divide(1, sin).eval()
    }
}

class Cot(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val tan = Tan(get(0)).eval()
        if (tan is Tan)
            return this

        return Divide(1, tan).eval()
    }
}