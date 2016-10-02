package org.cerion.symcalc.expression;


public class ListExpr extends Expr {

	public ListExpr()
	{
	}
	
	public ListExpr(Expr t)
	{
		setArg(0, t);
	}
	
	public ListExpr(Expr ...e)
	{
		setArgs(e);
	}
	
	public void add(Expr t) 
	{ 
		addArg(t);
	}
	
	//Inherited
	public ExprType GetType() { return ExprType.LIST; }
	
	public void show(int i) 
	{
		indent(i,"List: " + size());
		for(int j = 0; j < size(); j++)
			get(j).show(i+1);
	}
	
	public String toString() 
	{ 
		String ret = "{";
		for(int i = 0; i < (int) size(); i++) {
			if(i > 0) ret += ","; //dont add comma on first element
			//Eval before printing
			ret += get(i).eval().toString();
		}
		
		ret += "}";
		return ret; 
	}

	public ListExpr eval() 
	{
		return this;
	}
}
