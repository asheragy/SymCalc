package org.cerion.symcalc.expression;

import org.cerion.symcalc.expression.constant.Pi;
import org.cerion.symcalc.expression.number.RealNum;

import java.util.Hashtable;

public abstract class ConstExpr extends Expr {

	/*
	public ConstExpr(String s) {
		Name n = lookup(s);
		if(n != null)
			setValue(n);
		else
			throw new IllegalArgumentException("invalid constant");
	}
	*/

	public static boolean isConstant(String s) {
		return (lookup(s) != null);
	}

	public static ConstExpr getConstant(String name) {
		Name n = lookup(name);
		switch(n) {
			case PI: return new Pi();
		}

		throw new IllegalArgumentException("invalid constant");
	}

	@Override
	public ExprType getType() {
		return ExprType.CONST;
	}

	@Override
	public abstract  String toString();

	public void show(int i) {
        indent(i,"Constant: " + toString());
	}

	@Override
	public boolean equals(Expr e) {
		return getClass() == e.getClass();
	}

	private enum Name {
		PI,
		E
	}

	private static Hashtable<String, Name> identifiers = null;
	static 
    {
    	identifiers = new Hashtable<>();
    	identifiers.put("pi", Name.PI);
		identifiers.put("e", Name.E);
    }

	private static Name lookup(String s) {
		String lookup = s.toLowerCase();
		if(identifiers.containsKey(lookup))
			return identifiers.get(lookup);

		return null;
	}

	@Override
	protected int getProperties() {
		return Properties.CONSTANT.value;
	}
}
