package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.Function;
import org.cerion.symcalc.expression.function.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

import java.util.Random;

public class RandomInteger extends FunctionExpr {

	public RandomInteger(int max) {
		this(new IntegerNum(max));
	}

	public RandomInteger(Expr ...e) {
		super(Function.RANDOM_INTEGER, e);
	}
	
	@Override
	protected Expr evaluate() {

		if(size() == 0) { //Default no parameters is random 0 or 1
			return new IntegerNum( getRandomInteger(0, 1));
		} else if(get(0).isInteger()) {
			int N = ((IntegerNum)get(0)).intValue();
			return new IntegerNum( getRandomInteger(0, N));
		} else if (get(0).isList()) {
			int min = getList(0).getInteger(0).intValue();
			int max = getList(0).getInteger(1).intValue();
			return new IntegerNum( getRandomInteger(min, max));
		}

		/* Other cases
		[range, n]
		[range, {n1,n2,...}]
		[dist]
		 */

		return this;
	}

	private int getRandomInteger(int min, int max) {
		Random rand = new Random();
		int diff = max - min;

		return rand.nextInt(diff + 1) + min;
	}

	/*
	private static IntegerNum getRandom(IntegerNum max) {
		return getRandom(IntegerNum.ZERO,max);
	}

	private static IntegerNum getRandom(IntegerNum min, IntegerNum max) {
		//TODO this uses bits and we need a certain range, be easy to fix when testing smaller values
		int maxBits = max.toBigInteger().bitLength();
		return new IntegerNum(new BigInteger(maxBits,new Random()));
	}
	*/
}