package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum

class Partition(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.PARTITION, *e) {

    override fun evaluate(): Expr {

        val input = get(0) as ListExpr
        val n = (get(1) as IntegerNum).intValue()
        val result = ListExpr()

        for (i in 0 until input.size() / n) {
            val sublist = ListExpr()

            for (j in 0 until n) {
                val pos = i * n + j
                if (pos >= input.size())
                    break

                sublist.add(input[pos])
            }

            result.add(sublist)
        }

        return result
    }

    @Throws(ValidationException::class)
    override fun validate() {
        /*
        // TODO make generic parameter errors so they can be changed if needed

        if(size() < 2) {
            return new ErrorExpr("missing parameter {2} length");
        } else if (!get(1).isInteger()) {
            return new ErrorExpr("parameter {2} must be an integer");
        } else if (!get(0).isList()) {
            return new ErrorExpr("parameter {1} must be a list");
        }

        return null;
        */
    }

}
