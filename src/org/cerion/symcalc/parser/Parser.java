package parser;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.MathConst;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.function.Divide;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.function.Plus;
import org.cerion.symcalc.expression.function.Power;
import org.cerion.symcalc.expression.function.Subtract;
import org.cerion.symcalc.expression.function.Times;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.NumberExpr;

public class Parser {

	public Expr e;

	public Parser(Lexer lex) {
		this.lex = lex;
		getNext(); //start input
		this.e = expr();
	}
	/*
	  expr = factor
	       | expr + term
	       | expr - term
	
	  term = expterm
	       | term * expterm
	       | term / expterm
	
   expterm = factor
           | expterm ^ factor
    
	factor = integer
	       |  x
	       | - expr
	       | expr!
	       | foo(expr)
	       | (expr)
	*/
    private Expr expr() { 

    	Expr e = term();                   
       
        while (lex.hasInput()) {
            if (token == '+') {
                getNext();
                e = new Plus(e, term());
                
            } 
            else if (token == '-') {
                getNext();
                e = new Subtract(e,term());
            } 

            else {

                return e;
            }

        }


        
        return e;
    }

    private Expr term() {                    
    	Expr e = expterm();        
         
        while (lex.hasInput()) {
            if (token == '*') {
                getNext();
                e = new Times(e,expterm());
            } 

            else if (token == '/') {
                getNext();
                e = new Divide(e,expterm());
            } 
            else {
                return e;
            }
        }

        return e;
    }
    
    private Expr expterm() {                    
    	Expr e = factor();                   

        while (lex.hasInput()) 
        {
            if (token == '^') 
            {
                getNext();
                e = new Power(e,factor());
            } 

            else 
            {
                return e;
            }
        }


        return e;
    }
    private Expr factor() 
    { 

    	if ( token == NUMBER) 
    	{ 
            //if (lex.hasInput()) 
            {
            	getNext();
            	
            }
    		//Number f = new Number(tokval);
            NumberExpr f = NumberExpr.parse(tokval);
            return f;
        }
    	else if(token == 'i') //complex 1
    	{
    		getNext();
    		NumberExpr img = NumberExpr.parse("i");
    		return img;
    	}

    	else if (token == 'v') 
    	{   
    		//if(lex.hasInput()) 
    			getNext();
            return new VarExpr(tokval);
    	}
        else if (token == '-') 
        {
        	getNext();
        	Expr next = factor();
        	//if number just negate it
        	if(next.isNumber())
        		next = ((NumberExpr)next).negate();
        	else
        		next = new Times(new IntegerNum("-1"),next);
        	
        	return next;
        }
		else if (token == '{') {
			getNext();
			ListExpr ml = new ListExpr(expr());

			while(token == ',') 
			{
				getNext();
				ml.add(expr());
			}
			if(token != '}')
				System.out.println("Edit: missing }");
			getNext();
			return ml;
		}
    
    	else if (token == FUNC) {
    		String ident = tokval;
    		getNext();
    		if (token != '(')
    			System.out.println("missing ident (");
    		getNext();

    		FunctionExpr mf = FunctionExpr.CreateFunction(ident, expr() );
    		
			while(token == ',') //Add additional parameters
			{
				getNext();
				mf.add(expr());
			}
    		
    		if (token == ')') 
    		{
    			getNext();
    			return mf;
    		}
    		else 
    			System.out.println("missing ident )");

    	}
        else if (token == CONST) 
        {
			getNext();
            return new MathConst(tokval);
        } 
        else if (token == '(') 
        {
        	
            getNext();
            Expr e = expr();
            if (token == ')') 
            {
            	getNext();
            	return e;
            } 
            else 
            {
                System.out.println("missing ')'");
            }
            return e;

        } 
        
        else {
        	
            System.out.println("syntax error");
        }

        System.out.println("error: return null");
        return null; /* not used */
    }
    
	private Lexer lex;

	private char token;
	private String tokval;
	public static final char NUMBER = '0';
	public static final char CONST = 'c';
	public static final char FUNC = 'f';
	
    private void getNext() {
    	token = lex.getToken();
    	tokval = lex.tokval;
    }
}
