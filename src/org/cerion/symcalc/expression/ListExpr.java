package org.cerion.symcalc.expression;


import java.util.List;

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
	
	public void add(Expr t) {
		addArg(t);
	}

	public void addAll(List<Expr> items) {
		if(items != null) {
			for (Expr e : items) {
				add(e);
			}
		}
	}

	@Override
	public boolean equals(Expr e) {
		if(e.isList()) {
			ListExpr list = (ListExpr)e;
			if(list.size() == size()) {
				for(int i = 0; i < list.size(); i++) {
					if(!list.get(i).equals(get(i)))
						return false;
				}

				return true;
			}
		}

		return false;
	}

	//Inherited
	public ExprType getType() { return ExprType.LIST; }
	
	public void show(int i) 
	{
		indent(i,"List: " + size());
		for(int j = 0; j < size(); j++)
			get(j).show(i+1);
	}
	
	public String toString() 
	{ 
		String ret = "{";
		for(int i = 0; i < size(); i++) {
			if(i > 0) ret += ","; //dont add comma on first element
			//Eval before printing
			ret += get(i).toString();
		}
		
		ret += "}";
		return ret; 
	}

	@Override
	protected ListExpr evaluate() {
		return this;
	}

}
