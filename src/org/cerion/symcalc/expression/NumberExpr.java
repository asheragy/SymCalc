package org.cerion.symcalc.expression;

import org.cerion.symcalc.expression.number.ComplexNum;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;

public abstract class NumberExpr extends Expr
{
	//Types
	public static final int INTEGER = 0;
	public static final int RATIONAL = 1;
	public static final int REAL = 2;
	public static final int COMPLEX = 3;
	public abstract int numType(); //getType already used by MathTerm
	
	public abstract String toString();
	public abstract NumberExpr add(NumberExpr num);
	public abstract NumberExpr subtract(NumberExpr num);
	public abstract NumberExpr multiply(NumberExpr num);
	public abstract NumberExpr divide(NumberExpr num);
	public abstract NumberExpr power(NumberExpr num);
	public abstract boolean canExp(NumberExpr num); //this^num = num is TRUE, FALSE if can't resolve
	public abstract NumberExpr negate();

	public abstract double toDouble(); //Valid on all but ComplexNum
	public abstract boolean isZero();
	public abstract boolean isOne();
	public abstract boolean equals(NumberExpr e);

	//Members
	//private BigInteger n;

	@Override
	public boolean equals(Expr e) {
		return e.isNumber() && equals((NumberExpr)e);
	}

	@Override
	public boolean isInteger() {
		return numType() == INTEGER;
	}

	public boolean isReal() {
		return numType() == REAL;
	}

	public boolean isRational() {
		return numType() == RATIONAL;
	}

	public boolean isComplex() {
		return numType() == COMPLEX;
	}

	//Inherited from MathTerm
	public ExprType getType() { return ExprType.NUMBER; }
	public void show(int i) 
	{
		indent(i,"Number " + this.toString());
	}

	@Override
	public NumberExpr evaluate() {
		return this;
	}

	public static NumberExpr parse(String s) {
		if(s.indexOf('i') > -1)
			return new ComplexNum(s);

		if(s.indexOf('.') > 0)
			return RealNum.parse(s);

		return new IntegerNum(s);
	}
}
