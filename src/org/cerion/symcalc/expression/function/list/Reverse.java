package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;

public class Reverse extends FunctionExpr {

	public Reverse(Expr... e) {
		super(FunctionType.REVERSE,e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isList())
		{
			ListExpr L = (ListExpr)get(0);
			ListExpr result = new ListExpr();
			
			for(int i = L.size(); i > 0; i--)
				result.add(L.get(i-1));
			
			return result;
		}
	
		return this;
	}
}
