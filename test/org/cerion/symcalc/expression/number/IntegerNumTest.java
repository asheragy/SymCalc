package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.NumberExpr;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IntegerNumTest {

	private final NumberExpr i0 = new IntegerNum(0);
	private final NumberExpr i1 = new IntegerNum(1);
	private final NumberExpr i3 = new IntegerNum(3);
	private final NumberExpr i5 = new IntegerNum(5);
	private final NumberExpr in = new IntegerNum(-17);
	private final NumberExpr big = new IntegerNum("99999999999999999999999999999999999999999999");
	
	@Test
	public void stringConstructor()
	{
		verify(new IntegerNum("0"), 0);
		verify(new IntegerNum("5"), 5);
		verify(new IntegerNum("-5"), -5);
		verify(new IntegerNum("1234567890"), 1234567890);
		verify(new IntegerNum("-1234567890"), -1234567890);
		
		verify(new IntegerNum("9999999999999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999999999999"));
	}

	@Test
	public void constants()
	{
		verify(IntegerNum.ZERO, 0);
		verify(IntegerNum.ONE, 1);
		verify(IntegerNum.TWO, 2);
	}
	
	@Test
	public void negate()
	{
		verify(IntegerNum.ZERO.negate(), 0);
		verify(IntegerNum.ONE.negate(), -1);
		verify(IntegerNum.TWO.negate(), -2);
		verify(new IntegerNum(-5).negate(), 5);
	}
	
	@Test
	public void addition() 
	{
		//Identity
		verify(i0.add(i1), 1);
		verify(i1.add(i0), 1);
		
		//Basic
		verify(i1.add(i1), 2);
		verify(i0.add(i0), 0);
		verify(i3.add(i5), 8);
		
		//Negative
		verify(in.add(i5), -12);
		verify(i5.add(in), -12);
		verify(in.add(in), -34);
		
		//Big
		verify(big.add(big), new BigInteger("199999999999999999999999999999999999999999998"));
		verify(big.add(i1), new BigInteger("100000000000000000000000000000000000000000000"));
		verify(big.add(big.negate()), 0);
	}
	
	@Test
	public void subtraction()
	{
		//Zero
		verify(i0.subtract(i0), 0);
		verify(i0.subtract(i1), -1);
		verify(i1.subtract(i0), 1);
		
		//Basic
		verify(i1.subtract(i1), 0);
		verify(i3.subtract(i5), -2);
		verify(i5.subtract(i3), 2);
		
		//Negative
		verify(in.subtract(i5), -22);
		verify(i5.subtract(in), 22);
		verify(in.subtract(in), 0);
		
		//Big
		verify(big.subtract(big), new BigInteger("0"));
		verify(big.subtract(i1), new BigInteger("99999999999999999999999999999999999999999998"));
		verify(i1.subtract(big), new BigInteger("-99999999999999999999999999999999999999999998"));
		verify(big.subtract(big.negate()), new BigInteger("199999999999999999999999999999999999999999998"));
	}
	
	@Test 
	public void multiply()
	{
		//Zero
		verify(i0.multiply(i0), 0);
		verify(i0.multiply(i1), 0);
		verify(i1.multiply(i0), 0);
		
		//Basic
		verify(i1.multiply(i1), 1);
		verify(i3.multiply(i5), 15);
		
		//Negative
		verify(in.multiply(i5), -85);
		verify(i5.multiply(in), -85);
		verify(in.multiply(in), 289);
		
		//Big
		verify(big.multiply(big), new BigInteger("9999999999999999999999999999999999999999999800000000000000000000000000000000000000000001"));
		verify(big.multiply(i1), new BigInteger("99999999999999999999999999999999999999999999"));
		verify(i1.multiply(big), new BigInteger("99999999999999999999999999999999999999999999"));
		verify(big.multiply(big.negate()), new BigInteger("-9999999999999999999999999999999999999999999800000000000000000000000000000000000000000001"));
	}
	
	@Test
	public void divide()
	{
		//Zero
		divideByZero(i1, i0);
		divideByZero(i1, new RationalNum((IntegerNum)i0,(IntegerNum)i1));
		divideByZero(i1, RealNum.create(0));
		divideByZero(i1, new ComplexNum());

		//Add more later
	}
	
	@Test
	public void factorial() {
		verify(IntegerNum.factorial(0), 1);
		verify(IntegerNum.factorial(1), 1);
		verify(IntegerNum.factorial(2), 2);
		verify(IntegerNum.factorial(5), 120);
	}

	private void verify(NumberExpr e, long expected)
	{
		if(e.numType() != NumberExpr.INTEGER)
			fail("unexpected type: " + e.numType());
		
		IntegerNum n = (IntegerNum)e;
		assertEquals(expected, n.intValue());
	}
	
	private void verify(NumberExpr e, BigInteger expected)
	{
		if(e.numType() != NumberExpr.INTEGER)
			fail("unexpected type: " + e.numType());
		
		IntegerNum n = (IntegerNum)e;
		assertEquals(expected, n.toBigInteger());
	}
	
	private void divideByZero(NumberExpr n, NumberExpr exp)
	{
		try
		{
			verify(n.divide(exp), 0);
		}
		catch(ArithmeticException e)
		{
			//Success
		}
	}

}
