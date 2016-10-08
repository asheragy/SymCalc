package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

import java.math.BigInteger;
import java.util.Random;

public class RandomInteger extends FunctionExpr {

	public RandomInteger(Expr ...e) {
		super(FunctionType.RANDOM_INTEGER, e);
	}
	
	@Override
	protected Expr evaluate() {
		Random rand = new Random();

		if(size() == 0) { //Default no parameters is random 0 or 1
			return new IntegerNum( rand.nextInt(2) );
		} else if(get(0).isInteger())
			return getRandom( (IntegerNum)get(0));

		return this;
	}

	private static IntegerNum getRandom(IntegerNum max) {
		return getRandom(IntegerNum.ZERO,max);
	}

	private static IntegerNum getRandom(IntegerNum min, IntegerNum max) {
		//TODO this uses bits and we need a certain range, be easy to fix when testing smaller values
		int maxBits = max.toBigInteger().bitLength();
		return new IntegerNum(new BigInteger(maxBits,new Random()));
	}
}