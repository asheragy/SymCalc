package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class GCD extends FunctionExpr {

	public GCD(Expr ...e) {
		super(FunctionType.GCD, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		//TODO can take more than 2 integer parameters
	
		if(size() == 2 && get(0).isInteger() && get(1).isInteger())
		{
			IntegerNum a = (IntegerNum)get(0);
			IntegerNum b = (IntegerNum)get(1);
			
			return a.gcd(b);
		}
	
		return this;
	}
}
