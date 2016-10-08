package org.cerion.symcalc.expression;

import java.util.Hashtable;

import org.cerion.symcalc.expression.number.RealNum;

public class ConstExpr extends Expr {

	public ConstExpr(String s) {
		Name n = lookup(s);
		if(n != null)
			setValue(n);
		else
			throw new IllegalArgumentException("invalid constant");
	}

	@Override
	public ExprType getType() {
		return ExprType.CONST;
	}

	@Override
	public String toString() {
		return getValue().toString();
	}

	public void show(int i) {
        indent(i,"Constant[" + getValue() + "]");
	}

	@Override
    public Name getValue() {
    	return (Name)mValue;
    }

	@Override
	public boolean equals(Expr e) {
		if(e.isConst()) {
			ConstExpr c = (ConstExpr)e;
			if(c.getValue() == getValue())
				return true;
		}

		return false;
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

    public static boolean isConstant(String s) {
		return (lookup(s) != null);
	}

	private static Name lookup(String s) {
		String lookup = s.toLowerCase();
		if(identifiers.containsKey(lookup))
			return identifiers.get(lookup);

		return null;
	}

	@Override
	protected Expr evaluate() {

    	switch (getValue()) {
     		case PI :
     			return new RealNum(Math.PI);
			case E:
				return new RealNum(Math.E);
    	}
    	
    	return this;
	}
}
