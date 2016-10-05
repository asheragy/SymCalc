package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.NumberExpr;

public class Fibonacci extends FunctionExpr {

	public Fibonacci(Expr ...e) {
		super(FunctionType.FIBONACCI, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isInteger())
		{
			int n = ((IntegerNum)get(0)).toInteger();

			NumberExpr a = IntegerNum.ZERO;
			NumberExpr b = IntegerNum.ONE;
			NumberExpr fib = IntegerNum.ZERO;
			if(n==1) //first 2 values are 0 and 1 so don't start loop unless 2 or higher
				fib = new IntegerNum(1);
			for(int i = 1; i < n; i++)
			{
				fib = a.add(b);
				a = b;
				b = fib;
			}
			
			return fib;
		}

		return this;
	}
}


