package org.cerion.symcalc.expression;

public class ErrorExpr extends Expr {

	public void show(int i) 
	{
		indent(i,"Error " + this.toString());
	}
	
	public Expr eval() 
	{ 
		return this; 
	};


	
	public String value()
	{
		return (String)getValue();
	}
	
	public ErrorExpr(String error) 
	{
		setValue(error);
	}

	public ExprType GetType()
	{ 
		return ExprType.ERROR;
	}

	@Override
	public String toString() 
	{
		return "Error(" + value() + ")";
	}
}
