package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.*;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Table extends FunctionExpr {

	public Table(Expr... e) {
		super(FunctionType.TABLE);
		this.setArgs(e);
	}
	
	@Override
	protected Expr evaluate() {
		Expr expr = get(0);
		ListExpr argList = (ListExpr)get(1);

		if(argList.size() == 1)
			return evaluate(expr, null, IntegerNum.ONE, argList.getInteger(0), IntegerNum.ONE);

		VarExpr var = (VarExpr)argList.get(0);

		if (argList.size() == 2 && argList.get(1).isList())
			return evaluate(expr, var, argList.getList(1));
		else if (argList.size() == 2)
			return evaluate(expr, var, IntegerNum.ONE, argList.getInteger(1), IntegerNum.ONE);
		else if (argList.size() == 3)
			return evaluate(expr, var, argList.getInteger(1), argList.getInteger(2), IntegerNum.ONE);
		else if (argList.size() == 4)
			return evaluate(expr, var, argList.getInteger(1), argList.getInteger(2), argList.getInteger(3));

		return new ErrorExpr("Table() unexpected case");
	}

	private Expr evaluate(Expr expr, VarExpr var, IntegerNum iMin, IntegerNum iMax, IntegerNum iStep) {
		int min = iMin.intValue();
		int max = iMax.intValue();
		int step = iStep.intValue();

		ListExpr values = new ListExpr();
		for(int i = min; i <= max; i+= step)
			values.add(new IntegerNum(i));

		return evaluate(expr, var, values);
	}

	private Expr evaluate(Expr expr, VarExpr var, ListExpr values) {
		ListExpr result = new ListExpr();
		for(int i = 0; i < values.size(); i++) {
			if (var != null)
				getEnv().setVar(var.value(), values.get(i));
			result.add( expr.eval());
		}

		return result;
	}

	@Override
	protected int getProperties() {
		return Properties.HOLD.value;
	}

	@Override
	public void validate() throws ValidationException {
		 /*
			Table[expr,{N}]     		    //N copies
			Table[expr,{x, max}] 	        //1 to max
			Table[expr,{x, min,max}]      //min to max
			Table[expr,{x, min,max,step}] //min to max with step

			Example:
				Table[RandomInteger[10],{10}]
		 */

		validateParameterCount(2);
		validateParameterType(1, ExprType.LIST);

		ListExpr list = getList(1);
		if (list.size() == 0)
			throw new ValidationException("list parameters must not be empty");

		if (list.size() == 1)
			if(!list.get(0).isInteger())
				throw new ValidationException("list parameter at position 0 must be an integer");

		if (list.size() > 4)
			throw new ValidationException("too many list parameters");

		// If more than 1 parameter first must be variable and the rest integers
		if (list.size() > 1) {
			if (!list.get(0).isVariable())
				throw new ValidationException("first list parameter must be variable");

			if (list.size() >= 2 && (!list.get(1).isInteger() && !list.get(1).isList()))
				throw new ValidationException("first list parameter at position 1 must be integer OR value list");
			if (list.size() >= 3 && !list.get(2).isInteger())
				throw new ValidationException("first list parameter at position 2 must be integer");
			if (list.size() == 4 && !list.get(3).isInteger())
				throw new ValidationException("first list parameter at position 3 must be integer");
		}
	}
}
