package org.cerion.symcalc.expression;

public class BoolExpr extends Expr {

	public static final BoolExpr TRUE = new BoolExpr(true);
	public static final BoolExpr FALSE = new BoolExpr(false);
	
	public BoolExpr(boolean value)
	{
		setValue(value);
	}
	
	public boolean value() {
		return (boolean)this.getValue();
	}

	@Override
	public boolean equals(Expr e) {
		if(e.isBool()) {
			BoolExpr be = (BoolExpr) e;
			return be.value() == value();
		}

		return false;
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
	protected Expr evaluate() {
		return this;
	}

	@Override
	public ExprType getType() {
		return ExprType.BOOL;
	}

}
