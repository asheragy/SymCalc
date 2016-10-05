package org.cerion.symcalc.expression;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.expression.Expr;

public class VarExpr extends Expr {
	
	//Inherited
	public ExprType GetType()
	{ 
		return ExprType.VARIABLE;
	}
	
    public VarExpr(String str) 
    {
    	setValue(str);
    }

    @Override
    public void show(int i) 
    {
    	indent(i,"VarExpr = " + value());
    }
    
    public String value()
    {
    	return (String)getValue();
    }
    
    @Override
    public String toString()
    {
    	return value();
	}
    
    @Override
    protected Expr evaluate()
    {
    	Expr result = getEnv().getVar(value());
    	if(result == null)
    		result = this;
    	
    	return result;
    }

    @Override
    public boolean equals(Expr t)
    {
    	if(t.GetType() != ExprType.VARIABLE)
    		return false;
    	
    	if(value().contentEquals( ((VarExpr)t).value() ))
    		return true;
    	
    	return false;
    }

}