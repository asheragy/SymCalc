package org.cerion.symcalc.expression;

public class ErrorExpr extends Expr {

	public void show(int i) 
	{
		indent(i,"Error " + this.toString());
	}

	@Override
	protected Expr evaluate() {
		return this; 
	}

	@Override
	public boolean equals(Expr e) {
		// There should never be a reason to compare errors so just say they are never equal
		return false;
	}

	public String value()
	{
		return (String)getValue();
	}
	
	public ErrorExpr(String error) {
		setValue(error);
	}

	public static ErrorExpr getInvalidParameterType(Class c, int position) {
		String error = String.format("parameter {%d} must be a %s", position, c.getSimpleName());
		return new ErrorExpr(error);
	}

	public ExprType getType()
	{ 
		return ExprType.ERROR;
	}

	@Override
	public String toString() 
	{
		return "Error(" + value() + ")";
	}
}
