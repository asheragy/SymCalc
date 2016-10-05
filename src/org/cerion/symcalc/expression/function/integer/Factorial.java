package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Factorial extends FunctionExpr {

	public Factorial(Expr ...e) {
		super(FunctionType.FACTORIAL, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		//TODO can work on non-integers
	
		if(get(0).isInteger())
		{
			IntegerNum n = (IntegerNum)get(0);
			return n.factorial();
		}
		
		return this;
	}

}
