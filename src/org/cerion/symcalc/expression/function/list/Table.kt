package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.number.IntegerNum

class Table(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.TABLE, *e) {

    override fun evaluate(): Expr {
        val expr = get(0)
        val argList = get(1) as ListExpr

        if (argList.size() == 1)
            return evaluate(expr, null, IntegerNum.ONE, argList.getInteger(0), IntegerNum.ONE)

        val v = argList[0] as VarExpr

        if (argList.size() == 2 && argList[1].isList)
            return evaluate(expr, v, argList.getList(1))
        else if (argList.size() == 2)
            return evaluate(expr, v, IntegerNum.ONE, argList.getInteger(1), IntegerNum.ONE)
        else if (argList.size() == 3)
            return evaluate(expr, v, argList.getInteger(1), argList.getInteger(2), IntegerNum.ONE)
        else if (argList.size() == 4)
            return evaluate(expr, v, argList.getInteger(1), argList.getInteger(2), argList.getInteger(3))

        return ErrorExpr("Table() unexpected case")
    }

    private fun evaluate(expr: Expr, `var`: VarExpr?, iMin: IntegerNum, iMax: IntegerNum, iStep: IntegerNum): Expr {
        val min = iMin.intValue()
        val max = iMax.intValue()
        val step = iStep.intValue()

        val values = ListExpr()
        var i = min
        while (i <= max) {
            values.add(IntegerNum(i.toLong()))
            i += step
        }

        return evaluate(expr, `var`, values)
    }

    private fun evaluate(expr: Expr, `var`: VarExpr?, values: ListExpr): Expr {
        val result = ListExpr()
        for (i in 0 until values.size()) {
            if (`var` != null)
                env.setVar(`var`.value(), values[i])
            result.add(expr.eval())
        }

        return result
    }

    override val properties: Int
        get() = Properties.HOLD.value

    @Throws(ValidationException::class)
    override fun validate() {
        /*
			Table[expr,{N}]     		    //N copies
			Table[expr,{x, max}] 	        //1 to max
			Table[expr,{x, min,max}]      //min to max
			Table[expr,{x, min,max,step}] //min to max with step

			Example:
				Table[RandomInteger[10],{10}]
		 */

        validateParameterCount(2)
        validateParameterType(1, Expr.ExprType.LIST)

        val list = getList(1)
        if (list.size() == 0)
            throw ValidationException("list parameters must not be empty")

        if (list.size() == 1)
            if (!list[0].isInteger)
                throw ValidationException("list parameter at position 0 must be an integer")

        if (list.size() > 4)
            throw ValidationException("too many list parameters")

        // If more than 1 parameter first must be variable and the rest integers
        if (list.size() > 1) {
            if (!list[0].isVariable)
                throw ValidationException("first list parameter must be variable")

            if (list.size() >= 2 && !list[1].isInteger && !list[1].isList)
                throw ValidationException("first list parameter at position 1 must be integer OR value list")
            if (list.size() >= 3 && !list[2].isInteger)
                throw ValidationException("first list parameter at position 2 must be integer")
            if (list.size() == 4 && !list[3].isInteger)
                throw ValidationException("first list parameter at position 3 must be integer")
        }
    }
}
