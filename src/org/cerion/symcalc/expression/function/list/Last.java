package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;

public class Last extends FunctionExpr {

	public Last(Expr... e) {
		super(FunctionType.LAST,e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isList())
		{
			ListExpr L = (ListExpr)get(0);
			return L.get(L.size() - 1);
		}
		
		return this;
	}
}
