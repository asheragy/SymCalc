package org.cerion.symcalc.expression.number;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class RealNum extends NumberExpr
{
	public static RealNum parse2(String s) {
		// TODO add BigDecimal
		return new RealNum_Double(s);
	}

	public static RealNum create(IntegerNum n) {
		return new RealNum_Double(n);
	}

	public static RealNum create(RationalNum r) {
		return new RealNum_Double(r);
	}

	public static RealNum create(double n) {
		return new RealNum_Double(n);
	}

	public abstract boolean isWholeNumber(); // TODO better name for this? isInteger() is already taken
	public abstract IntegerNum toInteger();

	@Override
	public int compareTo(NumberExpr o) {
		throw new NotImplementedException();
	}

	/*
	public RealNum negate()
	{
		RealNum result = new RealNum();
		if(bigNumber != null)
			result.bigNumber = bigNumber.negate();
		else
			result.dNumber = -dNumber;
		return result;
	}
	*/

	/*
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
	*/
	
	public boolean canExp(NumberExpr num)
	{
		if(num.getNumType() == NumberType.COMPLEX)
			return false;
		return true;
	}

	/*
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
	*/
	
}