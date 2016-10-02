package org.cerion.symcalc.expression;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public abstract class Expr 
{
	private Object mValue;
	private List<Expr> mArgs;
	
	protected boolean EVAL = true;
	
	protected void setArgs(Expr ...args)
	{
		if(mArgs == null)
			mArgs = new ArrayList<>();
		
		for(Expr k : args)
			mArgs.add( EVAL ? k.eval() : k );
		
	}
	
	public List<Expr> getArgs()
	{
		return mArgs;
	}
	
	public Expr[] args()
	{
		return mArgs.toArray(new Expr[mArgs.size()]);
	}
	
	public Expr get(int index)
	{
		return mArgs.get(index).eval();
	}
	
	protected void setArg(int index, Expr e)
	{
		if(mArgs == null)
			mArgs = new ArrayList<>();
		
		if(index == mArgs.size())
			mArgs.add(EVAL ? e.eval() : e );
		else
			mArgs.set(index, EVAL ? e.eval() : e);
	}
	
	protected void addArg(Expr e)
	{
		setArg(size(), EVAL ? e.eval() : e);
	}
	
	public int size()
	{
		if(mArgs != null)
			return mArgs.size();
		
		return 0;
	}
	
	protected String argString()
	{
		return mArgs.toString();
	}
	
	public Object getValue()
	{
		return mValue;
	}
	
	protected void setValue(Object obj)
	{
		mValue = obj;
	}
	
	@Override
	public abstract String toString();

	@Override
	public boolean equals(Object obj) {

		if(obj instanceof Expr)
			return this.equals((Expr)obj);

		return false;
	}

	//TODO make required
	public boolean equals(Expr e) {
		return false;
	}

	//TODO, verify what everything below is for again
	public abstract void show(int i); //rename to something like treeForm or make different function, can probably make non-abstract if types are standardized (name + value/args)
	
	//TODO, add some optional verify() that can Override to see if eval will even work
	public abstract Expr eval();
	public abstract ExprType GetType();
	
	public void print()
	{
		show(0);
	}
	
	public enum ExprType
	{ 
		NUMBER,
		UNARY,
		VARIABLE,
		FUNCTION,
		LIST,
		CONST,
		ERROR,
		BOOL
	}

	protected void indent(int i, String s) 
	{
		for(int ii = 0; ii < 2*i; ii++)
			System.out.print(" ");
		System.out.println(s);
	}
	
	public boolean isNumber()
	{
		return GetType() == ExprType.NUMBER;
	}
	
    public boolean isInteger()
    {
    	if(isNumber() && ((NumberExpr)this).isInteger())
    		return true;
    	
    	return false;
    }
    
	public boolean isList()
	{
		return GetType() == ExprType.LIST;
	}
	
	//TODO, may be an actual function that does this
	public ListExpr toList(int size)
	{
		ListExpr result = new ListExpr();
		for(int i = 0; i < size; i++)
			result.add(this);
		
		return result;
	}

	//TODO, needs to be passed in recursively so it can be changed
	private static Hashtable<String, Expr> environment = new Hashtable<String, Expr>();
	
	public static void SetVar(String name, Expr value)
	{	
		environment.put(name,value);
	}
	
	public static Expr GetVar(String name)
	{
		return environment.get(name);
	}


}
