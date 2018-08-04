package org.cerion.symcalc.expression.function.trig;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.number.RealNum;

public class Sin extends TrigBase {

	public Sin(Expr ...e) {
		super(FunctionType.SIN, e);
	}

	@Override
	protected Expr evaluate(NumberExpr num) {
		if(!num.isComplex())
			return RealNum.create( Math.sin( num.toDouble() ));

		return this;
	}

}
