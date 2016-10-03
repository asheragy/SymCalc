package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.number.RealNum;

public class Sin extends FunctionExpr {

	public Sin(Expr ...e) {
		super(FunctionType.SIN, e);
	}

	@Override
	public Expr eval(Environment env) {
		
		if(size() == 1 && get(0).isNumber())
		{
			NumberExpr num = (NumberExpr)get(0);
			
			if(!num.isComplex() && env.isNumericalEval())
				return new RealNum( Math.sin( num.toDouble() ));
		}
		
		return this;
	}
}
