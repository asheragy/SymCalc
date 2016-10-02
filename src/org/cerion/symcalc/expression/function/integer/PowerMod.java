package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class PowerMod extends FunctionExpr {

	public PowerMod(Expr ...e) {
		super(FunctionType.POWERMOD, e);
	}
	
	@Override
	public Expr eval() {
		
		
		if(size() == 3 && get(0).isInteger() && get(1).isInteger() && get(2).isInteger())
		{
			IntegerNum a = (IntegerNum)get(0);
			IntegerNum b = (IntegerNum)get(1);
			IntegerNum c = (IntegerNum)get(2);
			return a.PowerMod(b, c);
		}
 		
 			
		return this;
	}
}
