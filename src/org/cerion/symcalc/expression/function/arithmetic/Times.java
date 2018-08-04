package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Times extends FunctionExpr {

	public Times(Expr... e) {
		super(FunctionType.TIMES,e);
	}
	
	@Override
	protected Expr evaluate() {
		
		NumberExpr product = IntegerNum.ONE;
		List<Expr> list = new ArrayList<>();

		for(int i = 0; i < size(); i++)
			list.add(get(i));

		//Multiply numbers
		Iterator<Expr> it = list.iterator();
		while(it.hasNext())
		{
			Expr e = it.next();
			if(e.isNumber())
			{
				NumberExpr n = (NumberExpr)e;
				if(!n.isOne())
					product = product.multiply(n);
				it.remove();
			}
		}
		
		//At least 1 non-number value
		if(list.size() > 0) {
			//Only add number if not one
			if(!product.isOne())
				list.add(0, product);

			if (list.size() == 1)
				return list.get(0);

			return new Times(list.toArray(new Expr[list.size()]));
		}
		
		return product;
	}
	
	@Override
	public String toString() {
		if(size() > 0)
		{
			String s = get(0).toString();
			for(int i = 1; i < size(); i++)
				s += " * " + get(i).toString();
			
			return s;
		}
		
		return super.toString();
	}

	@Override
	protected int getProperties() {
		return Properties.ASSOCIATIVE.value;
	}
}
