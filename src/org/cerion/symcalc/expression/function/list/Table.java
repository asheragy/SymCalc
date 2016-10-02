package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.ErrorExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Table extends FunctionExpr {

	public Table(Expr... e) {
		super(FunctionType.TABLE);
		this.EVAL = false;
		this.setArgs(e);
	}
	
	@Override
	public Expr eval() {
	
		//TODO, need to make sure expr is evaulated each time
		// Table[RandomInteger[10],{10}]
		
		//Table[expr,{N}]     		    //N copies
		//Table[expr,{x, max}] 	        //1 to max
		//Table[expr,{x, min,max}]      //min to max
		//Table[expr,{x, min,max,step}] //min to max with step
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
					varList.add(expr);
				
				//Set variable equal to list
				//SetVar(var.GetName(),varList);
				result = varList;
			}
			
			//Expr followed by at least 1 number
			else if(argList.size() >= 2 && argList.get(0).GetType() == ExprType.VARIABLE && argList.get(1).isNumber())
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
				SetVar(var.value(),varList);
				result = expr.eval();
				
				//If result is not a list, make it one
				if(result.GetType() != ExprType.LIST)
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
}
