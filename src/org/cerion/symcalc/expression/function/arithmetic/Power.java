package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.ErrorExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.NumberExpr;

public class Power extends FunctionExpr {

	public Power(Expr... e) {
		super(FunctionType.POWER,e);
	}

	@Override
	public Expr evaluate() {
		Expr a = get(0);
		Expr b = get(1);
		
		//Identity
		// TODO unit test and move to NumberExpr classes
		if(b.isNumber() && ((NumberExpr)b).isOne())
			return a;
				
		if(a.isNumber() && b.isNumber()) {
			NumberExpr n1 = (NumberExpr)a;
			NumberExpr n2 = (NumberExpr)b;

			return n1.power(n2);
		}

		return this;
	}
	
	@Override
	public String toString() {
		if(size() == 2)
			return get(0) + "^" + get(1);
		
		return super.toString();
	}

	@Override
	public void validate() throws ValidationException {
		validateParameterCount(2);
	}
}
