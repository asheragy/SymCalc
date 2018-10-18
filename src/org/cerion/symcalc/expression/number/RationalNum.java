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
		if(d.getSignum() == -1)
			set(n.unaryMinus(),d.unaryMinus());
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
		IntegerNum gcd = numerator().gcd(denominator());
		IntegerNum n;
		IntegerNum d;

		if(!gcd.isOne()) {
			n = (IntegerNum) numerator().div(gcd);
			d = (IntegerNum) denominator().div(gcd);
			set(n,d);
		}

		//Never allow bottom to be negative
		if(denominator().getSignum() == -1) {
			n = numerator().unaryMinus();
			d = denominator().unaryMinus();
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
	public NumberExpr unaryMinus() {
		return new RationalNum(numerator().unaryMinus(), denominator());
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
	public NumberExpr plus(NumberExpr num) {
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum + IntegerNum
				IntegerNum i = num.asInteger();

				IntegerNum norm = i.times(denominator());
				return new RationalNum(numerator().plus(norm), denominator());
				
			case RATIONAL: //RationalNum + RationalNum
			{
				RationalNum div = (RationalNum)num;
				IntegerNum n1 = div.numerator().times( denominator());
				IntegerNum n2 = numerator().times( div.denominator() );
				
				result = new RationalNum(n1.plus(n2), denominator().times(div.denominator()));
				return result.evaluate();
			}
		}
		
		return num.plus(this); //Default reverse order
	}

	@Override
	public NumberExpr minus(NumberExpr num) {
		NumberExpr negative = num.unaryMinus();
		return this.plus(negative);
	}

	@Override
	public NumberExpr times(NumberExpr num) {
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum * IntegerNum
			{
				result = new RationalNum(numerator().times(num.asInteger()), denominator());
				return result.evaluate();
			}
			case RATIONAL: //RationalNum * RationalNum
			{
				RationalNum t = (RationalNum)num;
				result = new RationalNum( numerator().times( t.numerator() ), denominator().times( t.denominator() ) );
				return result.evaluate();
			}
		}
		
		return num.times(this);
	}

	@Override
	public NumberExpr div(NumberExpr num) {
		RationalNum result;
		switch (num.numType()) 
		{
			case INTEGER: //RationalNum / IntegerNum
			{
				result = new RationalNum(numerator(), denominator().times( num.asInteger() ) );
				return result.evaluate();
			}
			case RATIONAL: //RationalNum / RationalNum
			{
				RationalNum t = (RationalNum)num;
				result = new RationalNum( numerator().times( t.denominator()), denominator().times(t.numerator()) );
				return result.evaluate();
			}
		}
		
		return num.times(this);
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