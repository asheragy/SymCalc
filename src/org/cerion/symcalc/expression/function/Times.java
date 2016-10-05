package org.cerion.symcalc.expression.function;

import java.util.Iterator;
import java.util.List;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.NumberExpr;

public class Times extends FunctionExpr {

	public Times(Expr... e) {
		super(FunctionType.TIMES,e);
	}
	
	@Override
	protected Expr evaluate() {
		
		NumberExpr product = IntegerNum.ONE;
		
		//Merge nexted Times functions
		List<Expr> list = getArgs();
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) instanceof Times)
			{
				list.addAll(list.get(i).getArgs());
				list.remove(i);
				i--;
			}
		}
		
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
		if(list.size() > 0)
		{
			//Only add number if not one
			if(!product.isOne())
				list.add(0, product);
			
			return new Times(list.toArray(new Expr[list.size()]));
		}
		
		return product;
	}
	
	@Override
	public String toString()
	{
		if(size() > 0)
		{
			String s = get(0).toString();
			for(int i = 1; i < size(); i++)
				s += " * " + get(i).toString();
			
			return s;
		}
		
		return super.toString();
	}
}
