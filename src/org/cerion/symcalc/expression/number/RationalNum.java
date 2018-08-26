package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.NumberExpr;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;

public class RationalNum extends NumberExpr
{
	public static final RationalNum ZERO = new RationalNum(IntegerNum.ZERO, IntegerNum.ONE);
	public static final RationalNum ONE = new RationalNum(IntegerNum.ONE, IntegerNum.ONE);

	public RationalNum(IntegerNum n) {
		this(n, IntegerNum.ONE);
	}

	public RationalNum(IntegerNum n, IntegerNum d) {
		if(d.signum() == -1)
			set(n.negate(),d.negate());
		else
			set(n,d);
	}

	public RationalNum(int n, int d) {
		this(new IntegerNum(n), new IntegerNum(d));
	}

	public IntegerNum numerator() {
		return (IntegerNum)get(0);
	}
	
	public IntegerNum denominator() {
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

	@Override
	public NumberExpr evaluate() {
		//Reduce with GCD
		IntegerNum gcd = numerator().GCD(denominator());
		IntegerNum n;
		IntegerNum d;

		if(!gcd.isOne()) {
			n = (IntegerNum) numerator().divide(gcd);
			d = (IntegerNum) denominator().divide(gcd);
			set(n,d);
		}

		//Never allow bottom to be negative
		if(denominator().signum() == -1) {
			n = numerator().negate();
			d = denominator().negate();
			set(n,d);
		}

		//Integer since denominator is one
		if(denominator().isOne()) {
			return numerator();
		}

		return this;
	}

	@Override
	public int numType() {
		return RATIONAL;
	}

	@Override
	public String toString() {
		return numerator().toString() + "/" + denominator().toString();
	}

	@Override
	public double toDouble() {
		BigDecimal decN = numerator().toBigDecimal();
		BigDecimal decD = denominator().toBigDecimal();
		decN = decN.divide(decD);
		return decN.doubleValue();
	}

	@Override
	public NumberExpr negate() {
		return new RationalNum(numerator().negate(), denominator());
	}

	public NumberExpr reciprocal() {
		return new RationalNum(denominator(), numerator()).evaluate();
	}

	@Override
	public boolean isZero() {
		return numerator().isZero();
	}

	@Override
	public boolean isOne() {
		return numerator().equals(denominator());
	}

	@Override
	public NumberExpr add(NumberExpr num) {
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum + IntegerNum
				IntegerNum i = (IntegerNum)num;

				IntegerNum norm = i.multiply(denominator());
				return new RationalNum(numerator().add(norm), denominator());
				
			case RATIONAL: //RationalNum + RationalNum
			{
				RationalNum div = (RationalNum)num;
				IntegerNum n1 = div.numerator().multiply( denominator());
				IntegerNum n2 = numerator().multiply( div.denominator() );
				
				result = new RationalNum(n1.add(n2), denominator().multiply(div.denominator()));
				return result.evaluate();
			}
		}
		
		return num.add(this); //Default reverse order
	}

	@Override
	public NumberExpr subtract(NumberExpr num) {
		NumberExpr negative = num.negate();
		return this.add(negative);
	}

	@Override
	public NumberExpr multiply(NumberExpr num) {
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum * IntegerNum
			{
				result = new RationalNum(numerator().multiply( (IntegerNum)num), denominator());
				return result.evaluate();
			}
			case RATIONAL: //RationalNum * RationalNum
			{
				RationalNum t = (RationalNum)num;
				result = new RationalNum( numerator().multiply( t.numerator() ), denominator().multiply( t.denominator() ) );
				return result.evaluate();
			}
		}
		
		return num.multiply(this);
	}

	@Override
	public NumberExpr divide(NumberExpr num) {
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum / IntegerNum
			{
				result = new RationalNum(numerator(), denominator().multiply( (IntegerNum)num ) );
				
				return result.evaluate();
			}
			case RATIONAL: //RationalNum / RationalNum
			{
				RationalNum t = (RationalNum)num;
				result = new RationalNum( numerator().multiply( t.denominator()), denominator().multiply(t.numerator()) );
				return result.evaluate();
			}
		}
		
		return num.multiply(this);
	}

	@Override
	public boolean canExp(NumberExpr num) {
		if(num.numType() == RATIONAL || num.numType() == COMPLEX)
			return false;

		return true;
	}

	@Override
	public Expr power(NumberExpr num) {
		IntegerNum n;
		IntegerNum d;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum ^ IntegerNum
				n = (IntegerNum) numerator().power( num );
				d = (IntegerNum) denominator().power( num );
				return new RationalNum(n,d);
			
			//case RATIONAL: //RationalNum ^ RationalNum
			//	break;
				
			case REAL: //RationalNum ^ RealNum
			{
				RealNum rResult = RealNum.create(this);
				return rResult.power(num);
			}
		}
	
		return null;
	}

	private void set(IntegerNum n, IntegerNum d) {
		if(n != null)
			setArg(0, n);
		if(d != null)
			setArg(1, d);
	}

	@Override
	public int compareTo(NumberExpr o) {
		throw new NotImplementedException();
	}
}