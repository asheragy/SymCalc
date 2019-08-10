package expression.function.logical

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import kotlin.math.min

class Equal(vararg e: Expr) : FunctionExpr(Function.EQUAL, *e) {

    override fun evaluate(): Expr {
        if (size == 1)
            return BoolExpr.TRUE

        var a = get(0)
        var b = get(1)
        if (a.isNumber && b.isNumber) {
            val minPrecision = min(a.precision, b.precision)
            if (minPrecision < a.precision)
                a = a.eval(minPrecision)
            if (minPrecision < b.precision)
                b = b.eval(minPrecision)

            return BoolExpr(a.equals(b))
        }

        return this
    }

    override fun validate() {
    }
}
