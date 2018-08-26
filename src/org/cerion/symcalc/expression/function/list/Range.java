package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Range extends FunctionExpr {

	public Range(Expr... e) {
		super(FunctionType.RANGE,e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isInteger())
		{
			int num = ((IntegerNum)get(0)).intValue();
			
			ListExpr listResult = new ListExpr();
			int i = 1; //default start at 1
			
			//if 2nd parameter range is num1 to num2
			if(size() > 1 && get(1).isInteger()) 
			{
				i = ((IntegerNum)get(0)).intValue();
				num = ((IntegerNum)get(1)).intValue();
			}
			
			//Step by 1 or 3rd parameter
			int iStep = 1;
			if(size() > 2 && get(2).isInteger())
				iStep = ((IntegerNum)get(2)).intValue();
				
			while(i <= num)
			{
				listResult.add(new IntegerNum(i));
				i = i + iStep;
			}
		
			return listResult;
		}

		return this;
	}
}
