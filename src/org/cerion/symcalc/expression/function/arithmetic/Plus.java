package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

import java.util.Iterator;
import java.util.List;

public class Plus extends FunctionExpr {

	public Plus(Expr... e) {
		super(FunctionType.PLUS,e);
	}
	
	@Override
	protected Expr evaluate() {
		
		NumberExpr sum = IntegerNum.ZERO;
		List<Expr> list = getArgs();

		Iterator<Expr> it = list.iterator();
		while(it.hasNext())
		{
			Expr e = it.next();
			if(e.isNumber())
			{
				sum = sum.add((NumberExpr)e);
				it.remove();
			}
		}

		//At least 1 non-number value
		if(list.size() > 0)
		{
			//Only add number if non-zero
			if(!sum.isZero())
				list.add(0, sum);

			//If only 1 entry just return it
			if(list.size() == 1)
				return list.get(0);
			
			return new Plus(list.toArray(new Expr[list.size()]));
		}
		
		return sum;
	}
	
	@Override
	public String toString() {
		if(size() > 0)
		{
			String s = get(0).toString();
			for(int i = 1; i < size(); i++)
				s += " + " + get(i).toString();
			
			return s;
		}
		
		return super.toString();
	}

	@Override
	protected int getProperties() {
		return Properties.ASSOCIATIVE.value;
	}
}
