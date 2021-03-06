package org.cerion.symcalc.function.logical

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.NumberExpr

class Greater(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        /*
            More than 2 elements are expanded to a > b > c > ...
            Simplified when possible
                 x > 2 > 1         = x > 2
                 x > 5 > 3 > 1 > y = x > 5 > 1 > y
         */

        // Simplify
        val args = args.toMutableList()

        run {
            var i = 0
            while (i < args.size - 1) {
                val comp = compare(args[i], args[i + 1])
                if (comp === LogicalCompare.TRUE) {
                    if (i == 0) { //First
                        args.removeAt(0)
                        i--
                    } else if (i == size - 2) { //Last
                        args.removeAt(i + 1)
                    } else {
                        //If the next is also true, we can remove current
                        if (compare(args[i + 1], args[i + 2]) === LogicalCompare.TRUE) {
                            args.removeAt(i + 1)
                            i--
                        }
                    }
                }
                i++
            }
        }

        // If elements were removed evaluate new instance
        if (args.size < size)
            return Greater(*args.toTypedArray()).eval()

        if (size == 1)
            return BoolExpr.TRUE
        else {

            for (i in 0 until size - 1) {
                val comp = compare(get(i), get(i + 1))
                if (comp === LogicalCompare.ERROR)
                    return ErrorExpr("invalid comparison")
                if (comp === LogicalCompare.FALSE)
                    return BoolExpr.FALSE
                if (comp === LogicalCompare.UNKNOWN)
                    return this
            }

            return BoolExpr.TRUE
        }
    }

    private fun compare(e1: Expr, e2: Expr): LogicalCompare {

        if (e1 is NumberExpr && e2 is NumberExpr) {
            if (e1 is Complex || e2 is Complex)
                return LogicalCompare.ERROR

            return if (e1 > e2)
                LogicalCompare.TRUE
            else
                LogicalCompare.FALSE
        }

        return LogicalCompare.UNKNOWN
    }

    override fun validate() {
    }
}
