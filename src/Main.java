
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.ComplexNum;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RationalNum;
import org.cerion.symcalc.expression.number.RealNum;

public class Main 
{
	public static void main(String args[]) 
	{
		// Test commit 3/8/17
		//TODO, if its a function name, it can't be a variable name
		//MathExpr expr = new MathExpr("d");
		//MathExpr expr = new MathExpr("FFT({-1, -1, -1, -1, 1, 1, 1, 1})");
		//MathExpr expr = new MathExpr("RandomInteger(10)");
		//Expr expr = Expr.parse("Greater(1,3)");
		//MathExpr expr = new MathExpr("N(Sin(1))");
		Expr expr = Expr.parse("Plot(x*x,{x,0,10})");

		expr.print();
		
		//expr.eval();
		Expr a = expr.eval();
		//Expr.ExprType t = a.getType();
		
		System.out.println("---------");
		System.out.println("Answer = " + a.toString()  );
		a.print();
	}
	
	public static void testNumbers()
	{
		NumberExpr[] nums = new NumberExpr[6];
		nums[0] = IntegerNum.ONE;
		nums[1] = RationalNum.ONE;
		nums[2] = RealNum.create(1.0);
		nums[3] = new ComplexNum(nums[0],nums[0]);
		nums[4] = new ComplexNum(nums[1],nums[1]);
		nums[5] = new ComplexNum(nums[2],nums[2]);
		
		//Add both numbers to each other
		for(int i = 0; i < 6; i++)
		{
			for(int j = 0; j < 6; j++)
			{
				NumberExpr t = nums[i].multiply(nums[j]);
				System.out.println(nums[i].numType() + " " + nums[j].numType() + " = " + t.toString());
			}
		}

		Expr c1 = Expr.parse("1 + 10i");
		Expr c2 = Expr.parse("2 - 3I");
		
		Expr exp = new Plus(c1.eval(), c2.eval());
		exp.show(0);
		exp.eval().show(0);
	}
}
