package org.cerion.symcalc.expression.function;

import static org.junit.Assert.*;

import org.cerion.symcalc.expression.function.Subtract;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.MathExpr;
import org.cerion.symcalc.expression.VarExpr;

public class SubtractTest {

	@Test
	public void parser() {
		//Verify a-b = Subtract(a,b)
		MathExpr expr = new MathExpr("5-1");
		IntegerNum e = (IntegerNum)expr.eval();
		assertEquals(4, e.toInteger());
	}
	
	@Test
	public void invalidParameters() {
		Expr e = get();
		assertTrue(e.isError());
		
		e = get(new IntegerNum(5));
		assertTrue(e.isError());
		
		e = get(new IntegerNum(1), new IntegerNum(2), new IntegerNum(3));
		assertTrue(e.isError());
		
		//List sizes
		ListExpr a = new IntegerNum(0).toList(2);
		ListExpr b = new IntegerNum(0).toList(3);
		
		e = get(a,b);
		assertTrue(e.isError());
	}
	
	@Test
	public void basicNumbers() {
		
		IntegerNum t = getI(new IntegerNum(5), new IntegerNum(3));
		assertEquals(2, t.toInteger());
		
		t = getI(new IntegerNum(3), new IntegerNum(5));
		assertEquals(-2, t.toInteger());
	}

	@Test
	public void nested() {
		Expr e = new Subtract(new IntegerNum(1), new IntegerNum(2));
		e = new Subtract(new IntegerNum(5), e);

		assertEquals(new IntegerNum(6), e.eval());
	}
	
	@Test
	public void identity() {
		Expr e = get(new VarExpr("a"), new IntegerNum(0));
		
		assertTrue(new VarExpr("a").equals(e));
	}
	
	@Test
	public void lists() {
		
		ListExpr a = new IntegerNum(10).toList(5);
		IntegerNum i = new IntegerNum(3);
		
		//List - Single
		Expr e = get(a,i);
		assertTrue(e.isList());
		
		ListExpr l = (ListExpr)e;
		assertEquals(5, l.size());
		
		for(int n = 0; n < 5; n++)
			assertEquals(7, ((IntegerNum)l.get(n)).toInteger());
		
		//Single - List
		e = get(i,a);
		
		l = (ListExpr)e;
		assertEquals(5, l.size());
		
		for(int n = 0; n < 5; n++)
			assertEquals(-7, ((IntegerNum)l.get(n)).toInteger());
	}

	
	private Expr get(Expr ...e)
	{
		return new Subtract(e).eval();
	}
	
	private IntegerNum getI(Expr ...e)
	{
		Expr t = new Subtract(e).eval();
		
		return (IntegerNum)t;
	}
}
