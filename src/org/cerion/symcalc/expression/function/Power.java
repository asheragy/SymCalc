package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.expression.ErrorExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.NumberExpr;

public class Power extends FunctionExpr {

	public Power(Expr... e) {
		super(FunctionType.POWER,e);
	}

	@Override
	public Expr eval() {
		
		if(size() != 2)
			return new ErrorExpr("invalid parameters");
		
		Expr a = get(0);
		Expr b = get(1);
		
		//Identity
		if(b.isNumber() && ((NumberExpr)b).isOne())
			return a;
				
		if(a.isNumber() && b.isNumber())
		{
			NumberExpr n1 = (NumberExpr)a;
			NumberExpr n2 = (NumberExpr)b;
			
			return n1.power(n2);
		}

		return this;
	}
	
	@Override
	public String toString()
	{
		if(size() == 2)
			return get(0) + "^" + get(1);
		
		return super.toString();
	}
}
