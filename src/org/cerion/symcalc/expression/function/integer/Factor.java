package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Factor extends FunctionExpr {

	public Factor(Expr ...e) {
		super(FunctionType.FACTOR, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isInteger())
		{
			IntegerNum num = (IntegerNum)get(0);
		
			ListExpr list = new ListExpr();
			while(num.IsEven())
			{
				num = (IntegerNum)num.divide( IntegerNum.TWO );
				list.add( IntegerNum.TWO);
			}
			if(num.toInteger() != 1)
				list.add(num);

			return list;
		}
 				
		return this;
	}
}
