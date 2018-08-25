package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.function.arithmetic.Times;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Factor extends FunctionExpr {

	public Factor(Expr ...e) {
		super(FunctionType.FACTOR, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isInteger()) {
			IntegerNum num = (IntegerNum)get(0);
		
			ListExpr list = new ListExpr();
			while(num.IsEven()) {
				num = (IntegerNum)num.divide( IntegerNum.TWO );
				list.add( IntegerNum.TWO);
			}

			// Continue factoring 3+
			if(!num.isOne()) {
				IntegerNum test = new IntegerNum(3);
				IntegerNum max = new Times(test, test).eval().toIntegerNum();

				while(max.compareTo(num) <= 0) {

					IntegerNum mod = num.mod(test);
					if (mod.isZero()) {
						list.add(test);
						num = (IntegerNum)num.divide(test);
					} else {
						test = new Plus(test, IntegerNum.TWO).eval().toIntegerNum();
						max = new Times(test, test).eval().toIntegerNum();
					}
				}

				list.add(num);
			}

			return list;
		}
 				
		return this;
	}
}
