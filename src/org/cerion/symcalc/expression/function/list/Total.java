package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;

public class Total extends FunctionExpr {

	public Total(Expr... e) {
		super(FunctionType.TOTAL,e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isList())
		{
			ListExpr e = (ListExpr)get(0);
			return new Plus(e.args()).eval();
		}

		return this;
	}
}
