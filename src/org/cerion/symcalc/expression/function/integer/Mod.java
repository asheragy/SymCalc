package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Mod extends FunctionExpr {

	public Mod(Expr ...e) {
		super(FunctionType.MOD, e);
	}
	
	@Override
	public Expr eval() {
		
		//TODO can work on non-integers
	
		if(size() == 2 && get(0).isInteger() && get(1).isInteger())
		{
			IntegerNum a = (IntegerNum)get(0);
			IntegerNum b = (IntegerNum)get(1);
			
			return a.mod(b);
		}
		
		
		return this;
	}
}
