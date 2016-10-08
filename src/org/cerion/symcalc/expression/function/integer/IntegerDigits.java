package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class IntegerDigits extends FunctionExpr {

	public IntegerDigits(Expr ...e) {
		super(FunctionType.INTEGER_DIGITS, e);
	}
	
	@Override
	public Expr evaluate() {
		
		if(get(0).isInteger())
		{
			IntegerNum num = (IntegerNum)get(0);
			ListExpr list = new ListExpr();
			
			String s = num.toString();
			for(int i = 0; i < s.length(); i++)
				list.add( NumberExpr.parse("" + s.charAt(i)));
			
			return list;	
		}
 				
		return this;
	}
}
