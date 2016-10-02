package parser;

import org.cerion.symcalc.expression.MathConst;
import org.cerion.symcalc.expression.FunctionExpr;

//import java.util.Hashtable;


public class Lexer {

	public String tokval;
	
	public Lexer(String exprStr) {
		pos = -1;
		               //remove WhiteSpace
		this.exprStr = exprStr.replaceAll(" ", "");
	
	}
	
	public char getToken() {
		
		pos++;
		if(pos >= exprStr.length())
			return 0;
		
		char c = exprStr.charAt(pos);
		
		switch (c) {
			case '+' : 
			case '-' :
			case '*' :
			case '/' :
			//case 'x' : 
			//case 'y' :
			case 'i' : //complex number 1
			case '(' :
			case ')' :
			case '[' :
			case ']' :
			case '{' :
			case '}' :
			case '^' :
			case ',' :
			case '!' : 
				return c;
				
			default : 
				if (Character.isDigit(c) || c == '.' || c == 'i') 
				{
					tokval = Character.toString(c);
					
					while (hasInput() && (Character.isDigit( exprStr.charAt(pos+1)) || exprStr.charAt(pos+1) == '.' || exprStr.charAt(pos+1) == 'i')) {
						pos++;
						tokval = tokval + exprStr.charAt(pos);
					}
					return Parser.NUMBER;
				}
			   if (Character.isLetter(c)) 
			   {
					tokval = Character.toString(c);
					while (hasInput() && Character.isLetter( exprStr.charAt(pos+1))) 
					{
						pos++;
						tokval = tokval + exprStr.charAt(pos);
					}
					if( isIdent(tokval) )
					{
						tokval = tokval.toLowerCase();
						return 'f'; 
					}
					else if (isConst(tokval))
					{
						tokval = tokval.toLowerCase();
						return 'c';
					}
					
					tokval = tokval.toLowerCase();
					return 'v'; //variable
						
	
					//System.out.println("Invalid identifier: " + tokval);
			   }
		}
		
		tokval = "error";
		return 'e';
	}
	
	public boolean hasInput() {
		if(pos+1 >= exprStr.length())
			return false;
		
		return true;
	}


	private String exprStr;
	private int pos;
	
	private boolean isIdent(String s) 
	{
		return FunctionExpr.isFunction(s);
	}
	
	private boolean isConst(String s) {
		if(MathConst.identifiers.containsKey(s.toLowerCase()))
			return true;
		return false;
	
	}

	
}
