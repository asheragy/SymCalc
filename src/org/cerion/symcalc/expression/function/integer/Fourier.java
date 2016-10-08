package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.NumberExpr;

public class Fourier extends FunctionExpr {

	public Fourier(Expr ...e) {
		super(FunctionType.FOURIER, e);
	}
	
	@Override
	protected Expr evaluate() {
		
		if(get(0).isList())
			return Recursive_FFT((ListExpr)get(0));
		
		return this;
	}
	

    public ListExpr Recursive_FFT(ListExpr list) 
    {
    	int N = list.size();
    	if(N == 1)
    		return list;
    	
    	System.out.println("FFT: " + list.toString());
    	ListExpr result = new ListExpr();
    	ListExpr a0 = new ListExpr();
    	ListExpr a1 = new ListExpr();
    	for(int i = 0; i < N; i = i + 2)
    	{
    		a0.add(list.get(i));
    		a1.add(list.get(i+1));
    	}
    	
    	NumberExpr w = NumberExpr.parse("1");
    	NumberExpr wN = NumberExpr.parse("1i");
    	
    	ListExpr y0 = Recursive_FFT(a0);
    	ListExpr y1 = Recursive_FFT(a1);
    	//MathList y0 = new MathList();
    	//MathList y1 = new MathList();
    	//y0.add(Number.getNumber("4"));
    	//y0.add(Number.getNumber("-2"));
    	//y1.add(Number.getNumber("2"));
    	//y1.add(Number.getNumber("-2"));
    	
    	System.out.println("y0 = " + y0.toString());
    	System.out.println("y1 = " + y1.toString());
    	
    	for(int k = 0; k < y0.size(); k++)
    	{
    		NumberExpr num1 = (NumberExpr)y0.get(k);
    		NumberExpr num2 = w.multiply( (NumberExpr)y1.get(k) );
    		result.add( num1.add(num2) );
    		result.add( num1.subtract(num2));
    		
    		w = w.multiply(wN);
    		//w2 = w2.multiply(Number.getNumber("-1"));
    	}
    	
    	
    	//Fix order
    	ListExpr temp = new ListExpr();
    	for(int i = 0; i < y0.size(); i++)
    	{
    		temp.add(result.get(i));
    		temp.add(result.get(i + y0.size()));
    	}
    	result = temp;
    	
    	return result;
    	
    }
}
