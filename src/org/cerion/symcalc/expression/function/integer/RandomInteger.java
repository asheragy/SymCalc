package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class RandomInteger extends FunctionExpr {

	public RandomInteger(Expr ...e) {
		super(FunctionType.RANDOM_INTEGER, e);
	}
	
	@Override
	public Expr eval() {
		
		if(get(0).isInteger())
			return IntegerNum.Random( (IntegerNum)get(0));

		return this;
	}
}