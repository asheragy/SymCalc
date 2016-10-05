package org.cerion.symcalc.expression;

import org.cerion.symcalc.Environment;

import java.util.ArrayList;
import java.util.List;

public abstract class Expr 
{
	private Object mValue;
	private List<Expr> mArgs;

	protected void setArgs(Expr ...args)
	{
		if(mArgs == null)
			mArgs = new ArrayList<>();
		
		for(Expr k : args) {
			if(k != null)
				mArgs.add(k);
		}
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
		return mArgs.get(index);
	}
	
	protected void setArg(int index, Expr e)
	{
		if(mArgs == null)
			mArgs = new ArrayList<>();
		
		if(index == mArgs.size())
			mArgs.add(e);
		else
			mArgs.set(index, e);
	}
	
	protected void addArg(Expr e)
	{
		setArg(size(), e);
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

	//Required subclass methods
	@Override
	public abstract String toString();
	//TODO, verify what everything below is for again
	public abstract void show(int i); //rename to something like treeForm or make different function, can probably make non-abstract if types are standardized (name + value/args)
	protected abstract Expr evaluate();
	public abstract ExprType GetType();

	public final Expr eval() {

		// Set environment for every parameter to the current one before its evaluated
		for (int i = 0; i < size(); i++) {
			get(i).setEnv(getEnv());
		}

		// Skip eval for Hold property
		if(!hasProperty(Properties.HOLD)) {
			for (int i = 0; i < size(); i++) {
				setArg(i, get(i).eval());

				//Return the first error
				if(get(i).isError())
					return get(i);
			}
		}

		// Listable property
		if(hasProperty(Properties.LISTABLE) && isFunction()) {
			FunctionExpr function = (FunctionExpr)this;
			if(size() == 1 && get(0).isList()) {
				ListExpr p1 = (ListExpr)get(0);
				ListExpr result = new ListExpr();

				for(int i = 0; i < p1.size(); i++)
					result.add(FunctionExpr.CreateFunction(function.getName(), p1.get(i)));

				return result.eval();
			}
		}

		//TODO if anything is an error the entire function should return an error and skip the normal eval
		// TODO, add some optional verify() that can Override to see if eval will even work, this will also return an error instead of normal eval

		return evaluate();
	}

	@Override
	public boolean equals(Object obj) {

		if(obj instanceof Expr)
			return this.equals((Expr)obj);

		return false;
	}

	//TODO make required
	public abstract boolean equals(Expr e);


	public void print()
	{
		show(0);
	}

	//TODO just make a function isXXX() for each type and make protected
	public enum ExprType {
		NUMBER,
		VARIABLE,
		FUNCTION,
		LIST,
		CONST,
		ERROR,
		BOOL
	}

	protected enum Properties {
		NONE(0),
		HOLD(1),
		LISTABLE(2);

		public final int value;

		Properties(final int newValue) {
			value = newValue;
		}
	}

	protected int getProperties() {
		return Properties.NONE.value;
	}

	private boolean hasProperty(Properties attr) {
		int attrs = getProperties();

		if((attr.value & attrs) != 0)
			return true;

		return false;
	}

	protected void indent(int i, String s) 
	{
		for(int ii = 0; ii < 2*i; ii++)
			System.out.print(" ");
		System.out.println(s);
	}
	
	public boolean isNumber() {
		return GetType() == ExprType.NUMBER;
	}
	
    public boolean isInteger() {
    	if(isNumber() && ((NumberExpr)this).isInteger())
    		return true;
    	
    	return false;
    }
    
	public boolean isList() {
		return GetType() == ExprType.LIST;
	}

	public boolean isFunction() {
		return GetType() == ExprType.FUNCTION;
	}

	public boolean isFunction(String name) {
		if(isFunction()) {
			FunctionExpr e = (FunctionExpr)this;
			if(name.compareToIgnoreCase(e.getName()) == 0)
				return true;
		}

		return false;
	}

	public boolean isError() {
		return GetType() == ExprType.ERROR;
	}
	
	//TODO, may be an actual function that does this
	public ListExpr toList(int size) {
		ListExpr result = new ListExpr();
		for(int i = 0; i < size; i++)
			result.add(this);
		
		return result;
	}

	public Environment getEnv() {
		return mEnv;
	}

	private void setEnv(Environment env) {
		mEnv = env;
	}

	private Environment mEnv = new Environment();


}
