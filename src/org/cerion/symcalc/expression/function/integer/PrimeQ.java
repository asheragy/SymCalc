package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class PrimeQ extends FunctionExpr {

	public PrimeQ(Expr ...e) {
		super(FunctionType.PRIMEQ, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isInteger())
		{
			IntegerNum n = (IntegerNum)get(0);
			if(n.PrimeQ())
				return BoolExpr.TRUE;
			else
				return BoolExpr.FALSE;
		}
			
		return this;
	}
}

