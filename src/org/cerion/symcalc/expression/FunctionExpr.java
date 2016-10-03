package org.cerion.symcalc.expression;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.expression.function.*;
import org.cerion.symcalc.expression.function.integer.*;
import org.cerion.symcalc.expression.function.list.*;

public abstract class FunctionExpr extends Expr
{
	private String mName;
	private FunctionType mType;

    protected FunctionExpr(FunctionType t, Expr... e) {
		mType = t;
    	mName = t.toString();

        setArgs(e);
    }
    
	@Override
	public ExprType GetType() {
		return ExprType.FUNCTION;
	}
	
	@Override
	public String toString() {
		return mName + argString();
	}

	public String getName() {
		return mName;
	}
	
	@Override
    public void show(int i) 
    {
        indent(i,mName + "[]");
		for(int j = 0; j < size(); j++)
			get(j).show(i+1);
    }

	@Override
	public boolean equals(Expr e) {
		if(e.GetType() != ExprType.FUNCTION)
			return false;

		FunctionExpr f = (FunctionExpr)e;
		if(f.mType != mType)
			return false;

		if(f.size() != size())
			return false;

		for(int i = 0; i < size(); i++) {
			if(!get(i).equals(f.get(i)))
				return false;
		}

		return true;
	}

	@Deprecated
	public Expr eval() {
		return eval(new Environment());
	}

	@Override
	public Expr eval(Environment env) {
		//TODO this should be done in every sub class instead, can remove from here once that is done
		return eval();
	}

	public void add(Expr t)
    { 
    	setArgs(t); 
    }
       
	protected enum FunctionType
	{
		N("N"),
		PLUS("Plus"),
		SUBTRACT("Subtract"),
		TIMES("Times"),
		DIVIDE("Divide"),
		POWER("Power"),
		SIN("Sin"),
		//TODO Derivative
		
		//List Functions
		TOTAL("Total"),
		RANGE("Range"),
		REVERSE("Reverse"),
		FIRST("First"),
		LAST("Last"),
		TABLE("Table"),
		
		//IntegerNum
		FACTORIAL("Factorial"),
		MOD("Mod"),
		POWERMOD("PowerMod"),
		GCD("GCD"),
		FOURIER("Fourier"),
		PRIMEQ("PrimeQ"),
		FIBONACCI("Fibonacci"),
		FACTOR("Factor"),
		BINOMIAL("Binomial"),
		INTEGER_DIGITS("IntegerDigits"),
		RANDOM_INTEGER("RandomInteger"),
		BERNOULLI("Bernoulli"),

		
		ASDFDSF("DSFDS");
		
		
	    private String value;

	    FunctionType(final String value) 
	    {
	        this.value = value;
	    }

	    public String getValue() 
	    {
	        return value;
	    }

	    @Override
	    public String toString() 
	    {
	        return this.getValue();
	    }
	    
	}
	
	private static FunctionType stringToFunctionType(String functionName)
	{
		String name = functionName.toLowerCase();
		for (FunctionType t : FunctionType.values()) 
		{
			if(t.value.toLowerCase().contentEquals(name))
				return t;
		}
		
		return null;
	}
	
	public static boolean isValidFunction(String functionName)
	{
		boolean bResult = false;
		String name = functionName.toLowerCase();
		
		if(bResult == false)
		{
			if(stringToFunctionType(name) != null)
				bResult = true;
		}
		
		return bResult;
	}
	
    public static FunctionExpr CreateFunction(String f, Expr ...e)
    {
		FunctionType type = stringToFunctionType(f);
		
		switch(type)
		{
			case N: return new N(e);
			case PLUS: return new Plus(e);
			case SUBTRACT: return new Subtract(e);
			case TIMES: return new Times(e);
			case DIVIDE: return new Divide(e);
			case POWER: return new Power(e);
			case SIN: return new Sin(e);
			
			//List
			case TOTAL: return new Total(e);
			case RANGE: return new Range(e);
			case REVERSE: return new Reverse(e);
			case FIRST: return new First(e);
			case LAST: return new Last(e);
			case TABLE: return new Table(e);

			//IntegerNum
			case FACTORIAL: return new Factorial(e);
			case MOD: return new Mod(e);
			case POWERMOD: return new PowerMod(e);
			case GCD: return new GCD(e);
			case FOURIER: return new Fourier(e);
			case PRIMEQ: return new PrimeQ(e);
			case FIBONACCI: return new Fibonacci(e);
			case FACTOR: return new Factor(e);
			case BINOMIAL: return new Binomial(e);
			case BERNOULLI: return new Bernoulli(e);
			case INTEGER_DIGITS: return new IntegerDigits(e);
			case RANDOM_INTEGER: return new RandomInteger(e);
			
			default:
				return null;
		}
    }
   
}