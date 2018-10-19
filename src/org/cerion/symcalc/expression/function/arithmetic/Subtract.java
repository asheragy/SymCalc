package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.expression.*;
import org.cerion.symcalc.expression.number.NumberExpr;

public class Subtract extends FunctionExpr {

	public Subtract(Expr...e) {
		super(FunctionType.SUBTRACT, e);
	}

	@Override
	public Expr evaluate() {
		
		if(size() != 2)
			return new ErrorExpr("invalid parameters");
		
		Expr a = get(0);
		Expr b = get(1);
		
		//Identity
		if(b.isNumber() && ((NumberExpr)b).isZero())
			return a;
				
		if(a.isNumber() && b.isNumber())
		{
			NumberExpr n1 = (NumberExpr)a;
			NumberExpr n2 = (NumberExpr)b;
			
			return n1.minus(n2);
		}
		else if(a.isList() || b.isList())
		{
			if(a.isList() && b.isList())
			{
				if(a.size() == b.size())
				{
					ListExpr result = new ListExpr();
					for(int i = 0; i < a.size(); i++)
					{
						Expr e = new Subtract(a.get(i), b.get(i));
						e = e.eval();
						result.add( e );
					}
					
					return result;
				}
				
				return new ErrorExpr("list sizes not equal");
			}
			else if(a.isList())
				return new Subtract(a, b.toList( a.size() )).eval();
			else 
				return new Subtract(a.toList( b.size()), b ).eval();
			
		}

		return this;
	}
	
	@Override
	public String toString()
	{
		if(size() == 2) {
			Expr e2 = get(1);
			return get(0) + " - " + (e2.isFunction("subtract") ? "(" + e2 + ")": e2);
		}
		
		return super.toString();
	}
}
