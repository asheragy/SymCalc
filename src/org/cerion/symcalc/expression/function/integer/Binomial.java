package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Binomial extends FunctionExpr {

	public Binomial(Expr ...e) {
		super(FunctionType.BINOMIAL, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isInteger() && get(1).isInteger())
		{
			IntegerNum n = (IntegerNum)get(0);
			IntegerNum k = (IntegerNum)get(1);
			
			// n! / k!(n-k)!
			IntegerNum n1 = n.factorial();
			IntegerNum n2 = n.subtract(k);
			n2 = n2.factorial();
			IntegerNum n3 = k.factorial();
			n2 = n2.multiply(n3);
			
			return n1.divide(n2);
		}
 			
		return this;
	}
	
}
