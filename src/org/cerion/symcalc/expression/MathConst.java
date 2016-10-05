package org.cerion.symcalc.expression;

import java.util.Hashtable;

import org.cerion.symcalc.expression.number.RealNum;
import org.cerion.symcalc.expression.Expr;

public class MathConst extends Expr {

	@Override
	public ExprType GetType()
	{
		return ExprType.CONST;
	}

	@Override
	public String toString()
	{
		return value();
	}

	public void show(int i) 
	{
        indent(i,"Constant = " + value());
	}

    public MathConst(String c)
    {
        setValue(c);
    }

    public String value()
    {
    	return (String)getValue();
    }

	@Override
	public boolean equals(Expr e) {
		return false; //TODO
	}

	private static enum Name
	{ 
		PI
	}
	
	public static Hashtable<String, Name> identifiers = null;
	static 
    {
    	identifiers = new Hashtable<String, Name>();
    	identifiers.put(new String("pi"), 	Name.PI);
    }
	
	
	
	@Override
	protected Expr evaluate()
    {
		Expr result = this;
		
    	switch (identifiers.get(value())) 
    	{
     		case PI : result = new RealNum(Math.PI);
     			
     		break;
    	}
    	
    	return result;
	}
}
