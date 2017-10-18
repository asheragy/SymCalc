package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.*;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Table extends FunctionExpr {

	public Table(Expr e, int N) {
		this(e, new ListExpr(new IntegerNum(N)));
	}

	public Table(Expr... e) {
		super(FunctionType.TABLE);
		//this.EVAL = false;
		this.setArgs(e);
	}
	
	@Override
	protected Expr evaluate() {
		/*
			Table[expr,{N}]     		    //N copies
			Table[expr,{x, max}] 	        //1 to max
			Table[expr,{x, min,max}]      //min to max
			Table[expr,{x, min,max,step}] //min to max with step

			Example:
				Table[RandomInteger[10],{10}]
		 */

		if(size() == 2 && get(1).isList())
		{
			Expr result = new ErrorExpr("Invalid parameters"); //Default
			Expr expr = get(0);
			
			//List needs to be {num} or {expr,num,num,num}
			ListExpr argList = (ListExpr)get(1);
			
			//Single number only
			if(argList.size() == 1 && argList.get(0).isNumber())
			{
				IntegerNum num = (IntegerNum)argList.get(0);
				int size = num.toInteger();

				ListExpr varList = new ListExpr();
				for(int i = 0; i < size; i++)
					varList.add(expr.eval());
				
				//Set variable equal to list
				//SetVar(var.GetName(),varList);
				result = varList;
			}
			
			//Expr followed by at least 1 number
			else if(argList.size() >= 2 && argList.get(0).isVariable() && argList.get(1).isNumber())
			{
				VarExpr var = (VarExpr)argList.get(0);
				IntegerNum n1 = (IntegerNum)argList.get(1);
				IntegerNum n2 = null;
				IntegerNum n3 = null;
				
				if(argList.size() >= 3 && argList.get(2).isNumber())
					n2 = (IntegerNum)argList.get(2);
				if(argList.size() >= 4 && argList.get(3).isNumber())
					n3 = (IntegerNum)argList.get(3);
				
				//Default 1 to max
				int a = 1;
				int b = n1.toInteger();
				
				//both min/max given
				if(n2 != null)
				{
					a = b;
					b = n2.toInteger();
				}

				int iStep = 1;
				if(n3 != null) //step
					iStep = n3.toInteger();
				
				ListExpr varList = new ListExpr();
				int i = a;
				while(i <= b)
				{
					varList.add( new IntegerNum(i));
					i += iStep;
				}
				
				//Set variable equal to list
				getEnv().setVar(var.value(),varList);
				result = expr.eval();
				
				//If result is not a list, make it one
				if(!result.isList())
				{
					varList = new ListExpr();
					i = a;
					while(i <= b)
					{
						varList.add( result);
						i += iStep;
					}
					result = varList.eval(); //eval might not be needed
				}
			}
			
			return result;
		}
			
		return this;
	}

	@Override
	protected int getProperties() {
		return Properties.HOLD.value;
	}
}
