package org.cerion.symcalc.expression;


import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.parser.Lexer;
import org.cerion.symcalc.parser.Parser;

//TODO, remove this and just use Expr as the public exposed element
public class MathExpr {

	private Expr e;
	
	public MathExpr(String e) {
		
		Lexer lex = new Lexer(e);
		Parser p = new Parser(lex);

		this.e = p.e;
	}
	
	public Expr eval() {
		//return e.eval(new Number());
		return e.eval();
	}
	
	public String toString()
	{
		Expr t = e.eval();
		String s = t.toString();
		return s;
	}
	//public Number eval(double x) {
	//	Double n = new Double(x);
		//return e.eval(new Number(n));
	//	return e.eval(-1);
	//}
	
	
	
	public void show() {
		e.show(0);
	}
	
}
