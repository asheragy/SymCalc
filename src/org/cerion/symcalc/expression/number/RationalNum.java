package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.NumberExpr;

import java.math.BigDecimal;

public class RationalNum extends NumberExpr
{
	public static final RationalNum ONE = new RationalNum(IntegerNum.ONE, IntegerNum.ONE);
	
	//TODO may want to only allow rational to be created with factory method since it can be reduced to integer, check mathematica for this
	public RationalNum(IntegerNum n, IntegerNum d) {
		if(d.signum() == -1)
			set(n.negate(),d.negate());
		else
			set(n,d);
	}

	public RationalNum(int n, int d) {
		this(new IntegerNum(n), new IntegerNum(d));
	}
	
	private RationalNum(NumberExpr n, NumberExpr d)
	{
		if(n.numType() != INTEGER || d.numType() != INTEGER)
			throw new IllegalArgumentException();
		
		set((IntegerNum)n,(IntegerNum)d);
	}
	
	public IntegerNum numerator()
	{
		return (IntegerNum)get(0);
	}
	
	public IntegerNum denominator()
	{
		return (IntegerNum)get(1);
	}

	@Override
	public boolean equals(NumberExpr e) {
		if(e.isRational()) {
			RationalNum r = (RationalNum) e;
			return numerator().equals(r.numerator()) && denominator().equals(r.denominator());
		}

		return false;
	}

	private void set(IntegerNum n, IntegerNum d)
	{
		if(n != null)
			setArg(0, n);
		if(d != null)
			setArg(1, d);
		
		//TODO, reduce
	}
	
	protected NumberExpr reduce() //Can return RationalNum OR IntegerNum
	{
		IntegerNum gcd = numerator().GCD(denominator());
		IntegerNum n;
		IntegerNum d;
		
		if(!gcd.isOne())
		{
			n = (IntegerNum) numerator().divide(gcd);
			d = (IntegerNum) denominator().divide(gcd);
			set(n,d);
		}
		
		if(denominator().signum() == -1) //Never allow bottom to be negative
		{
			n = numerator().negate();
			d = denominator().negate();
			set(n,d);
		}
		
		if(denominator().isOne())
		{
			return numerator();
		}
		
		return this;
	}
	
	public int numType() { return RATIONAL; }
	public String toString() { return numerator().toString() + "/" + denominator().toString(); }
	
	public double toDouble()
	{
		BigDecimal decN = numerator().toBigDecimal();
		BigDecimal decD = denominator().toBigDecimal();
		decN = decN.divide(decD);
		return decN.doubleValue();
	}
	
	public NumberExpr negate() 
	{
		return new RationalNum(numerator().negate(), denominator());
	}
	
	public boolean isZero()
	{
		return numerator().isZero();
	}
	
	public boolean isOne()
	{
		if(numerator().equals(denominator()))
			return true;
		
		return false;
	}
	
	public NumberExpr add(NumberExpr num) 
	{
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum + IntegerNum
				IntegerNum i = (IntegerNum)num;

				IntegerNum norm = (IntegerNum) i.multiply(denominator());
				return new RationalNum(numerator().add(norm), denominator());
				
			case RATIONAL: //RationalNum + RationalNum
			{
				RationalNum div = (RationalNum)num;
				IntegerNum n1 = (IntegerNum) div.numerator().multiply( denominator());
				IntegerNum n2 = (IntegerNum) numerator().multiply( div.denominator() );
				
				result = new RationalNum(n1.add(n2), denominator().multiply(div.denominator()));
				return result.reduce();
			}
		}
		
		return num.add(this); //Default reverse order
	}

	public NumberExpr subtract(NumberExpr num) 
	{
		NumberExpr negative = num.negate();
		return this.add(negative);
	}
	
	public NumberExpr multiply(NumberExpr num) 
	{
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum * IntegerNum
			{
				result = new RationalNum(numerator().multiply(num), denominator());
				return result.reduce();
			}
			case RATIONAL: //RationalNum * RationalNum
			{
				RationalNum t = (RationalNum)num;
				result = new RationalNum( numerator().multiply( t.numerator() ), denominator().multiply( t.denominator() ) );
				return result.reduce();
			}
		}
		
		return num.multiply(this);
	}

	public NumberExpr divide(NumberExpr num) 
	{
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum / IntegerNum
			{
				result = new RationalNum(numerator(), denominator().multiply( (IntegerNum)num ) );
				
				return result.reduce();
			}
			case RATIONAL: //RationalNum / RationalNum
			{
				RationalNum t = (RationalNum)num;
				result = new RationalNum( numerator().multiply( t.denominator()), denominator().multiply(t.numerator()) );
				return result.reduce();
			}
		}
		
		return num.multiply(this);
	}
	
	public boolean canExp(NumberExpr num)
	{
		if(num.numType() == RATIONAL)
			return false;
		if(num.numType() == COMPLEX)
			return false;
		return true;
	}
	
	public NumberExpr power(NumberExpr num) 
	{
		IntegerNum n;
		IntegerNum d;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum ^ IntegerNum
				n = (IntegerNum) numerator().power( num );
				d = (IntegerNum) denominator().power( (IntegerNum)num );
				return new RationalNum(n,d);
			
			//case RATIONAL: //RationalNum ^ RationalNum
			//	break;
				
			case REAL: //RationalNum ^ RealNum
			{
				RealNum rResult = new RealNum(this);
				return rResult.power(num);
			}
		}
	
		return null;
	}	

}