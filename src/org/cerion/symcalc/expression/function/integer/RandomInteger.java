package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

import java.util.Random;

public class RandomInteger extends FunctionExpr {

	public RandomInteger(Expr ...e) {
		super(FunctionType.RANDOM_INTEGER, e);
	}
	
	@Override
	public Expr eval(Environment env) {
		Random rand = new Random();

		if(size() == 0) { //Default no parameters is random 0 or 1
			return new IntegerNum( rand.nextInt(2) );
		} else if(get(0).isInteger())
			return IntegerNum.Random( (IntegerNum)get(0));

		return this;
	}
}