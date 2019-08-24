package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberType

// TODO add tests
class Range(vararg e: Expr) : FunctionExpr(Function.RANGE, *e) {

    override fun evaluate(): Expr {

        if (get(0).isInteger) {
            var num = (get(0) as Integer).intValue()

            val listResult = ListExpr()
            var i = 1 //default start at 1

            //if 2nd parameter range is num1 to num2
            if (size > 1 && get(1).isInteger) {
                i = (get(0) as Integer).intValue()
                num = (get(1) as Integer).intValue()
            }

            //Step by 1 or 3rd parameter
            var iStep = 1
            if (size > 2 && get(2).isInteger)
                iStep = (get(2) as Integer).intValue()

            while (i <= num) {
                listResult.add(Integer(i.toLong()))
                i += iStep
            }

            return listResult
        }

        return this
    }

    override fun validate() {
        //validateNumberType(0, NumberType.INTEGER)
    }
}
