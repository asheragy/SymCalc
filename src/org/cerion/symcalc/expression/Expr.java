package org.cerion.symcalc.expression;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.parser.Lexer;
import org.cerion.symcalc.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public abstract class Expr 
{
	protected Object mValue;
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
	
	public final int size() {
		if(mArgs != null)
			return mArgs.size();
		
		return 0;
	}
	
	protected String argString() {
		return mArgs.toString();
	}
	
	public Object getValue() {
		return mValue;
	}
	
	protected void setValue(Object obj) {
		mValue = obj;
	}

	public static Expr parse(String s) {
		Lexer lex = new Lexer(s);
		Parser p = new Parser(lex);
		return p.e;
	}

	public abstract String toString();
	public abstract void show(int i); //rename to something like treeForm or make different function, can probably make non-abstract if types are standardized (name + value/args)
	public abstract boolean equals(Expr e);
	protected abstract Expr evaluate();
	protected abstract ExprType getType();

	protected ErrorExpr validate() {
		return null;
	}

	public final Expr eval() {
		//https://reference.wolfram.com/language/tutorial/TheStandardEvaluationProcedure.html
		//https://reference.wolfram.com/language/tutorial/EvaluationOfExpressionsOverview.html

		if(validate() != null) {
			return validate();
		}

		// Associative function, if the same function is a parameter move its parameters to the top level
		if(hasProperty(Properties.ASSOCIATIVE)) {
			for(int i = 0; i < size(); i++) {
				if(get(i).getClass() == getClass()) {
					// insert these sub parameters at the same position it was removed
					Expr t = mArgs.get(i);
					mArgs.remove(i);
					mArgs.addAll(i,t.getArgs());
					i--;
				}
			}
		}

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

		return evaluate();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Expr && this.equals((Expr)obj);
	}

	public void print() {
		show(0);
	}

	protected enum ExprType {
		NUMBER,
		VARIABLE,
		FUNCTION,
		LIST,
		CONST,
		ERROR,
		BOOL
	}

	protected enum LogicalCompare {
		TRUE,
		FALSE,
		UNKNOWN,
		ERROR
	}

	protected enum Properties {
		NONE(0),
		HOLD(1),
		LISTABLE(2),
		ASSOCIATIVE(4);

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
	
	public final boolean isNumber() {
		return getType() == ExprType.NUMBER;
	}
	
    public boolean isInteger() {
    	if(isNumber() && ((NumberExpr)this).isInteger())
    		return true;
    	
    	return false;
    }
    
	public final boolean isList() {
		return getType() == ExprType.LIST;
	}

	public final boolean isFunction() {
		return getType() == ExprType.FUNCTION;
	}

	public final boolean isFunction(String name) {
		if(isFunction()) {
			FunctionExpr e = (FunctionExpr)this;
			if(name.compareToIgnoreCase(e.getName()) == 0)
				return true;
		}

		return false;
	}

	public final boolean isError() {
		return getType() == ExprType.ERROR;
	}

	public final boolean isVariable() {
		return getType() == ExprType.VARIABLE;
	}

	public final boolean isBool() {
		return getType() == ExprType.BOOL;
	}

	public final boolean isConst() {
		return getType() == ExprType.CONST;
	}
	
	//TODO, may be an actual function that does this
	public ListExpr toList(int size) {
		ListExpr result = new ListExpr();
		for(int i = 0; i < size; i++)
			result.add(this);
		
		return result;
	}

	public final Environment getEnv() {
		return mEnv;
	}

	private void setEnv(Environment env) {
		mEnv = env;
	}

	private Environment mEnv = new Environment();


}
