package org.cerion.symcalc.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer

class Table(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val expr = get(0)
        val argList = get(1) as ListExpr

        if (argList.size == 1)
            return evaluate(expr, null, Integer.ONE, argList.getInteger(0), Integer.ONE)

        val v = argList[0] as VarExpr

        if (argList.size == 2 && argList[1] is ListExpr)
            return evaluate(expr, v, argList.getList(1))
        else if (argList.size == 2)
            return evaluate(expr, v, Integer.ONE, argList.getInteger(1), Integer.ONE)
        else if (argList.size == 3)
            return evaluate(expr, v, argList.getInteger(1), argList.getInteger(2), Integer.ONE)
        else if (argList.size == 4)
            return evaluate(expr, v, argList.getInteger(1), argList.getInteger(2), argList.getInteger(3))

        return ErrorExpr("Table() unexpected case")
    }

    private fun evaluate(expr: Expr, x: VarExpr?, min: Integer, max: Integer, step: Integer): Expr {
        val values = (min.intValue() .. max.intValue() step step.intValue()).map { Integer(it) }
        return evaluate(expr, x, ListExpr(values))
    }

    private fun evaluate(expr: Expr, variable: VarExpr?, values: ListExpr): ListExpr {
        return ListExpr(values.args.map {
            if (variable != null)
                setEnvVar(variable.value, it)

            expr.eval()
        })
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
        validateParameterType(1, ExprType.LIST)

        val list = getList(1)
        if (list.size == 0)
            throw ValidationException("list parameters must not be empty")

        if (list.size == 1)
            if (list[0] !is Integer)
                throw ValidationException("list parameter at position 0 must be an integer")

        if (list.size > 4)
            throw ValidationException("too many list parameters")

        // If more than 1 parameter first must be variable and the rest integers
        if (list.size > 1) {
            if (list[0] !is VarExpr)
                throw ValidationException("first list parameter must be variable")

            if (list.size >= 2 && list[1] !is Integer && list[1] !is ListExpr)
                throw ValidationException("first list parameter at position 1 must be integer OR value list")
            if (list.size >= 3 && list[2] !is Integer)
                throw ValidationException("first list parameter at position 2 must be integer")
            if (list.size == 4 && list[3] !is Integer)
                throw ValidationException("first list parameter at position 3 must be integer")
        }
    }
}
