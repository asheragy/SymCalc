package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.FunctionExpr;

public class First extends FunctionExpr {

	public First(Expr... e) {
		super(FunctionType.LAST,e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isList())
		{
			ListExpr L = (ListExpr)get(0);
			return L.get(0);
		}
		
		return this;
	}
}
