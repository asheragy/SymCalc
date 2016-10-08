package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.NumberExpr;

import java.math.BigDecimal;

public class RealNum extends NumberExpr
{
	private BigDecimal bigNumber = null;
	//private BigDecimal test;
	private double dNumber = 0;
	
	public int numType() {
		return REAL;
	}
	
	public double toDouble()
	{
		if(bigNumber != null)
			return bigNumber.doubleValue();
		return dNumber;
	}
	public RealNum()
	{
		//bigNumber = new BigDecimal("0");
	}
	
	public RealNum(String s)
	{
		dNumber = Double.parseDouble(s);
		//test = null;
		//bigNumber = new BigDecimal(s);
	}
	
	public RealNum(IntegerNum n)
	{
		dNumber = n.toDouble();
	}
	
	public RealNum(RationalNum r)
	{
		dNumber = r.toDouble();
	}
	
	public RealNum(double n)
	{
		dNumber = n;
	}

	@Override
	public String toString() 
	{
		if(bigNumber != null)
			return bigNumber.toString();
		return "" + dNumber;
	}

	@Override
	public boolean equals(NumberExpr e) {
		if(e.isReal()) {
			RealNum n = (RealNum)e;

			if(n.bigNumber != null && bigNumber != null && n.bigNumber.compareTo(bigNumber) == 0)
				return true;

			if(n.bigNumber == null && bigNumber == null)
				return n.dNumber == dNumber;
		}

		return false;
	}
	
	public RealNum negate()
	{
		RealNum result = new RealNum();
		if(bigNumber != null)
			result.bigNumber = bigNumber.negate();
		else
			result.dNumber = -dNumber;
		return result;
	}
	
	public boolean isZero()
	{
		if(bigNumber != null)
			return bigNumber.equals(BigDecimal.ZERO);
		return (dNumber == 0.0);
	}
	
	public boolean isOne()
	{
		if(bigNumber != null)
			return bigNumber.equals(BigDecimal.ONE);
		return (dNumber == 1.0);
	}
	
	public NumberExpr add(NumberExpr num) 
	{
		RealNum result = new RealNum();
		switch (num.numType()) 
		{
			case INTEGER: //RealNum + IntegerNum

				//result.bigNumber = new BigDecimal( ((IntegerNum)num).integer );
				//result.bigNumber = result.bigNumber.add(this.bigNumber);
				result.dNumber = dNumber + num.toDouble();
				return result;
				
			case RATIONAL: //RealNum + RationalNum
			{
				result.dNumber = dNumber + num.toDouble();
				return result;
			}
			
			case REAL: //RealNum + RealNum
			{
				//result.bigNumber = this.bigNumber.add( ((RealNum)num).bigNumber );
				result.dNumber = dNumber + ((RealNum)num).dNumber;
				return result;
			}
						
		}
		
		return num.add(this);
	}
	
	public NumberExpr subtract(NumberExpr num) 
	{
		RealNum result = new RealNum();
		switch (num.numType()) 
		{
			case INTEGER: //RealNum - IntegerNum
			{
				//result.bigNumber = new BigDecimal( ((IntegerNum)num).integer );
				//result.bigNumber = bigNumber.subtract(result.bigNumber);
				result.dNumber = dNumber - num.toDouble();
				return result;
			}
			
			case RATIONAL: //RealNum - RationalNum
			{
				result.dNumber = dNumber - num.toDouble();
				return result;
			}
			
			case REAL: //RealNum - RealNum
			{
				//result.bigNumber = this.bigNumber.subtract( ((RealNum)num).bigNumber );
				result.dNumber = dNumber - ((RealNum)num).dNumber;
				return result;
			}
		}

		NumberExpr negative = num.negate();
		return negative.add(this);
	}
	
	public NumberExpr multiply(NumberExpr num) 
	{
		RealNum result = new RealNum();
		switch (num.numType()) 
		{
			case INTEGER: //RealNum * IntegerNum

				//result.bigNumber = new BigDecimal( ((IntegerNum)num).integer );
				//result.bigNumber = result.bigNumber.multiply(this.bigNumber);
				result.dNumber = dNumber * num.toDouble();
				return result;

			case RATIONAL: //RealNum * RationalNum
			{
				result.dNumber = dNumber * num.toDouble();
				return result;
			}
			
			case REAL: //RealNum * RealNum
			{
				//result.bigNumber = this.bigNumber.multiply( ((RealNum)num).bigNumber );
				result.dNumber = dNumber * ((RealNum)num).dNumber;
				return result;
			}
		}
		return num.multiply(this);
	}

	public NumberExpr divide(NumberExpr num) 
	{
		RealNum result = new RealNum();
		switch (num.numType()) 
		{
			case INTEGER: //RealNum / IntegerNum

				//result.bigNumber = new BigDecimal( ((IntegerNum)num).integer );
				//result.bigNumber = result.bigNumber.multiply(this.bigNumber);
				result.dNumber = dNumber / num.toDouble();
				return result;

			case RATIONAL: //RealNum / RationalNum
			{
				result.dNumber = dNumber / num.toDouble();
				return result;
			}
			
			case REAL: //RealNum / RealNum
			{
				//result.bigNumber = this.bigNumber.multiply( ((RealNum)num).bigNumber );
				result.dNumber = dNumber / ((RealNum)num).dNumber;
				return result;
			}
		}
		return num.multiply(this);
	}
	
	public boolean canExp(NumberExpr num)
	{
		if(num.numType() == COMPLEX)
			return false;
		return true;
	}
	
	public NumberExpr power(NumberExpr num) 
	{
		RealNum result = new RealNum();
		switch (num.numType()) 
		{
			case INTEGER: //RealNum ^ IntegerNum
			case RATIONAL: //RealNum ^ RationalNum
			case REAL: //RealNum ^ RealNum
			{
				result.dNumber = Math.pow( dNumber, num.toDouble());
				return result;
			}
		}
	
		return null;
	}	
	
}