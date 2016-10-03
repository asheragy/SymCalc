package org.cerion.symcalc.expression;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.expression.Expr;

public class BoolExpr extends Expr {

	public static final BoolExpr TRUE = new BoolExpr(true);
	public static final BoolExpr FALSE = new BoolExpr(false);
	
	private BoolExpr(boolean value)
	{
		setValue(value);
	}
	
	public boolean value() {
		return (boolean)this.getValue();
	}

	@Override
	public boolean equals(Expr e) {
		if(e.GetType() != ExprType.BOOL)
			return false;

		BoolExpr be = (BoolExpr)e;

		return be.value() == value();
	}

	@Override
	public String toString() {
		
		return (value() ? "True" : "False");
	}

	@Override
	public void show(int i) {
    	indent(i,"BoolExpr = " + toString());
	}

	@Override
	public Expr eval() {
		return this;
	}

	@Override
	public ExprType GetType() {
		return ExprType.BOOL;
	}

}
