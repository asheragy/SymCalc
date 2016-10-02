package org.cerion.symcalc.expression.function;

import java.util.Iterator;
import java.util.List;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.NumberExpr;

public class Plus extends FunctionExpr {

	public Plus(Expr... e) {
		super(FunctionType.PLUS,e);
	}
	
	@Override
	public Expr eval() {
		
		NumberExpr sum = IntegerNum.ZERO;
		
		//TODO reverse order so a+b+c is evaluated the same
		List<Expr> list = getArgs();
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) instanceof Plus)
			{
				list.addAll(list.get(i).getArgs());
				list.remove(i);
				i--;
			}
		}
		
		Iterator<Expr> it = list.iterator();
		while(it.hasNext())
		{
			Expr e = it.next().eval();
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
			
			return new Plus(list.toArray(new Expr[list.size()]));
		}
		
		return sum;
	}
	
	@Override
	public String toString()
	{
		if(size() > 0)
		{
			String s = get(0).toString();
			for(int i = 1; i < size(); i++)
				s += " + " + get(i).toString();
			
			return s;
		}
		
		return super.toString();
	}
}
