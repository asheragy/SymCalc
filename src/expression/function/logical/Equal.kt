package expression.function.logical

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Equal(vararg e: Expr) : FunctionExpr(Function.EQUAL, *e) {

    override fun evaluate(): Expr {
        if (size == 1)
            return BoolExpr.TRUE

        val a = get(0)
        val b = get(1)
        if (a.isNumber && b.isNumber) {
            return BoolExpr(a.asNumber().equals(b.asNumber()))
        }

        return this
    }

}
