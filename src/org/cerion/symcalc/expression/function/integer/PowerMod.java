package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class PowerMod extends FunctionExpr {

	public PowerMod(Expr ...e) {
		super(FunctionType.POWERMOD, e);
	}
	
	@Override
	public Expr evaluate() {
		if(get(0).isInteger() && get(1).isInteger() && get(2).isInteger())
		{
			IntegerNum a = (IntegerNum)get(0);
			IntegerNum b = (IntegerNum)get(1);
			IntegerNum c = (IntegerNum)get(2);
			return a.powerMod(b, c);
		}

		return this;
	}

	@Override
	public void validate() throws ValidationException {
		validateParameterCount(3);
		validateParameterType(0, ExprType.NUMBER);
		validateParameterType(1, ExprType.NUMBER);
		validateParameterType(2, ExprType.NUMBER);
	}
}
