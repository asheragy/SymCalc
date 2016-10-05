package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.NumberExpr;

public class Bernoulli extends FunctionExpr {

	public Bernoulli(Expr ...e) {
		super(FunctionType.BERNOULLI, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isInteger())
 		{
			Expr result;
			IntegerNum N = (IntegerNum)get(0);
			
 			if(N.isZero())
 				result = IntegerNum.ONE;
 			else if(N.isOne())
 				result = NumberExpr.parse("0.5").negate();
 			else
 			{
 				NumberExpr res = IntegerNum.ZERO;
 				int n = N.toInteger();
 				
 				for(int i = 0; i < n; i++)
 				{
 					FunctionExpr bin = new Binomial(new IntegerNum(n+1));
 					bin.add(new IntegerNum(i));
 					IntegerNum t = (IntegerNum)bin.eval();
 					//System.out.println(t.toString());
 					
 					FunctionExpr bern = new Bernoulli(new IntegerNum(i));
 					NumberExpr b = (NumberExpr)bern.eval();
 					//System.out.println(b);
 					
 					b = t.multiply(b);
 					
 					res = res.add(b);
 					
 				}
 				res = res.negate();
 				N = (IntegerNum) N.add(IntegerNum.ONE);
 				res = res.divide(N);
 				
 				result = res;	
 			}
 			
 			return result;	
 		}

		return this;
	}
	
}