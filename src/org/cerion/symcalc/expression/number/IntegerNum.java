package org.cerion.symcalc.expression.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.NumberExpr;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class IntegerNum extends NumberExpr
{
	public int numType() {
		return INTEGER; 
	};
	
	public IntegerNum(BigInteger n) {
		setValue(n);
	}
	
	public IntegerNum(String s) {
		setValue(new BigInteger(s)); 
	}
	
	public IntegerNum(long n) {
		setValue(BigInteger.valueOf(n));
	}

	@Override
	public String toString() {
		return val().toString(); 
	}

	@Override
	public boolean equals(NumberExpr e) {
		if(e.isInteger()) {
			IntegerNum n = (IntegerNum)e;
			if(val().compareTo(n.val()) == 0)
				return true;
		}

		return false;
	}

	public static final IntegerNum ZERO = new IntegerNum(0);
	public static final IntegerNum ONE = new IntegerNum(1);
	public static final IntegerNum TWO = new IntegerNum(2);
	public static final IntegerNum NEGATIVE_ONE = new IntegerNum(-1);
	
	public int toInteger() 
	{
		return val().intValue();
	}
	
	public BigInteger toBigInteger()
	{
		return val();
	}

	public double toDouble()
	{
		return val().doubleValue();
	}
	
	public RealNum toReal()
	{
		return null;
	}
	
	public BigDecimal toBigDecimal()
	{
		return new BigDecimal(val());
	}
	
	private BigInteger val()
	{
		return (BigInteger)getValue();
	}

	//TODO, move this out of here and into RandomInteger
	public static IntegerNum Random(IntegerNum max) { return Random(IntegerNum.ZERO,max); }
	public static IntegerNum Random(IntegerNum min, IntegerNum max)
	{
		//int minBits = min.val().bitLength();
		int maxBits = max.val().bitLength();
		
		return new IntegerNum(new BigInteger(maxBits,new Random()));
	}

	@Override
	public IntegerNum negate() {
		return new IntegerNum(val().negate());
	}

	public boolean isZero()
	{
		return val().equals(BigInteger.ZERO);
	}
	public boolean isOne()
	{
		return val().equals(BigInteger.ONE);
	}
	
	public int signum()
	{
		return val().signum();
	}

	public NumberExpr add(NumberExpr num)
	{
		if(num.numType() == INTEGER) //IntegerNum + IntegerNum
			return add((IntegerNum)num);
		
		return num.add(this); //Higher number type will handle conversion
	}
	
	public IntegerNum add(IntegerNum n)
	{
		return new IntegerNum( val().add( n.val() ));
	}
	
	public NumberExpr subtract(NumberExpr num) 
	{
		if(num.numType() == INTEGER) //IntegerNum - IntegerNum
			return subtract((IntegerNum)num);
		
		//Default reverse order
		NumberExpr negative = num.negate();
		return negative.add(this);
	}
	
	public IntegerNum subtract(IntegerNum n)
	{
		return new IntegerNum( val().subtract( n.val()) );
	}

	@Override
	public NumberExpr multiply(NumberExpr num) {
		if(num.numType() == INTEGER) //IntegerNum * IntegerNum
			return multiply((IntegerNum)num);

		return num.multiply(this);
	}
	
	public IntegerNum multiply(IntegerNum n) {
		return new IntegerNum( val().multiply( n.val() ));
	}

	public NumberExpr divide(NumberExpr num) 
	{
		//Any code calling this should check
		if(num.isZero())
			throw new ArithmeticException("divide by zero");
		
		if(num.isInteger()) //Int / Int
		{
			IntegerNum n = (IntegerNum)num;
			IntegerNum gcd = this.GCD(n);
			
			if(gcd.isOne())
				return new RationalNum(this, n);
			
			//Divide both by GCD
			IntegerNum a = new IntegerNum(val().divide(gcd.val()));
			IntegerNum b = new IntegerNum(n.val().divide(gcd.val()));
			
			if(b.isOne())
				return a;
			
			return new RationalNum(a,b);
		}
		
		throw new NotImplementedException();
		//return num.multiply(this);
	}	
	
	public boolean canExp(NumberExpr num)
	{
		switch (num.numType())
			{
			case INTEGER: return true;
			case REAL: return true;
			}
		
		return false;
	}
	
	public NumberExpr power(NumberExpr num) 
	{
		NumberExpr result = null;
		switch (num.numType()) 
		{
			case INTEGER: //IntegerNum ^ IntegerNum
				return new IntegerNum(val().pow( ((IntegerNum)num).val().intValue() ));

			case REAL: //RealNum ^ RealNum
			{
				result = new RealNum(this);
				break;
			}
		}
	
		return result.power(num);
	}	
	
	//IntegerNum Specific Functions
	public boolean IsEven()
	{
		return (val().testBit(0) == false);
	}
	
	public IntegerNum GCD(IntegerNum N)
	{
		return new IntegerNum(val().gcd(N.val()));
	}
	
	public IntegerNum PowerMod(NumberExpr b, NumberExpr m)
	{
		//Assuming all integers at this point since MathFunc needs to check that
		BigInteger num = ((IntegerNum)this).val();
		BigInteger exp = ((IntegerNum)b).val();
		BigInteger mod = ((IntegerNum)m).val();
	
		return new IntegerNum(num.modPow(exp, mod)) ;
	}
	
	public boolean PrimeQ()
	{
		return val().isProbablePrime(5);
	}
	
	public IntegerNum factorial()
	{ 
		return factorial((int)this.val().longValue()); 
	}
	
	public static IntegerNum factorial(int N)
	{
		BigInteger result = BigInteger.valueOf(1);
		while(N > 1)
		{
			result = result.multiply( new BigInteger(N + ""));
			N--;
		}

		return new IntegerNum(result);
	}
	
	public IntegerNum mod(IntegerNum N)
	{
		return new IntegerNum( val().mod(N.val()));
	}
}
