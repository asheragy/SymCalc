package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.function.arithmetic.Divide;
import org.cerion.symcalc.expression.function.arithmetic.Power;
import org.cerion.symcalc.expression.function.arithmetic.Times;
import org.cerion.symcalc.expression.function.integer.Factor;
import org.cerion.symcalc.expression.function.list.Tally;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IntegerNum extends NumberExpr {

	public static final IntegerNum ZERO = new IntegerNum(0);
	public static final IntegerNum ONE = new IntegerNum(1);
	public static final IntegerNum TWO = new IntegerNum(2);
	public static final IntegerNum NEGATIVE_ONE = new IntegerNum(-1);

	public IntegerNum(BigInteger n) {
		setValue(n);
	}
	
	public IntegerNum(String s) {
		setValue(new BigInteger(s)); 
	}
	
	public IntegerNum(long n) {
		setValue(BigInteger.valueOf(n));
	}

	@Override
	public int numType() {
		return INTEGER;
	}

	@Override
	public String toString() {
		return val().toString(); 
	}

	@Override
	public boolean equals(NumberExpr e) {
		if(e.isInteger()) {
			IntegerNum n = (IntegerNum)e;
			if(val().compareTo(n.val()) == 0)
				return true;
		}

		return false;
	}

	public int intValue() {
		return val().intValue();
	}

	@Deprecated
	public int toInteger() {
		return val().intValue();
	}
	
	public BigInteger toBigInteger() {
		return val();
	}

	public double toDouble() {
		return val().doubleValue();
	}
	
	public RealNum toReal() {
		return null;
	}
	
	public BigDecimal toBigDecimal() {
		return new BigDecimal(val());
	}
	
	private BigInteger val() {
		return (BigInteger)getValue();
	}

	@Override
	public IntegerNum negate() {
		return new IntegerNum(val().negate());
	}

	public boolean isZero() {
		return val().equals(BigInteger.ZERO);
	}

	public boolean isOne() {
		return val().equals(BigInteger.ONE);
	}
	
	public int signum() {
		return val().signum();
	}

	public NumberExpr add(NumberExpr num) {
		if(num.numType() == INTEGER) //IntegerNum + IntegerNum
			return add((IntegerNum)num);
		
		return num.add(this); //Higher number type will handle conversion
	}
	
	public IntegerNum add(IntegerNum n) {
		return new IntegerNum( val().add( n.val() ));
	}
	
	public NumberExpr subtract(NumberExpr num) {
		if(num.numType() == INTEGER) //IntegerNum - IntegerNum
			return subtract((IntegerNum)num);
		
		//Default reverse order
		NumberExpr negative = num.negate();
		return negative.add(this);
	}
	
	public IntegerNum subtract(IntegerNum n) {
		return new IntegerNum( val().subtract( n.val()) );
	}

	@Override
	public NumberExpr multiply(NumberExpr num) {
		if(num.numType() == INTEGER) //IntegerNum * IntegerNum
			return multiply((IntegerNum)num);

		return num.multiply(this);
	}
	
	public IntegerNum multiply(IntegerNum n) {
		return new IntegerNum( val().multiply( n.val() ));
	}

	public NumberExpr divide(NumberExpr num) {
		//Any code calling this should check
		if(num.isZero())
			throw new ArithmeticException("divide by zero");

        //Int / Int
		if(num.isInteger()) {
			IntegerNum n = (IntegerNum)num;
			IntegerNum gcd = this.GCD(n);
			
			if(gcd.isOne()) {
				if (getEnv().isNumericalEval()) {
					return (NumberExpr) new Divide(RealNum.create(toDouble()), RealNum.create(num.toDouble())).eval();
				}

				return new RationalNum(this, n);
			}
			
			//Divide both by GCD
			IntegerNum a = new IntegerNum(val().divide(gcd.val()));
			IntegerNum b = new IntegerNum(n.val().divide(gcd.val()));
			
			if(b.isOne())
				return a;

			if (getEnv().isNumericalEval()) {
				return (NumberExpr) new Divide(RealNum.create(a.toDouble()), RealNum.create(b.toDouble())).eval();
			}

			return new RationalNum(a,b);
		}
		
		throw new NotImplementedException();
		//return num.multiply(this);
	}

	public boolean canExp(NumberExpr num) {
		switch (num.numType())
			{
			case INTEGER: return true;
			case REAL: return true;
			}
		
		return false;
	}

    @Override
    public Expr power(NumberExpr num) {
		//NumberExpr result = null;
		switch (num.numType()) {
			case INTEGER:
				return new IntegerNum(val().pow( ((IntegerNum)num).val().intValue() ));

            case RATIONAL:
                double pow = Math.pow(val().doubleValue(), num.toDouble());
                RealNum real = RealNum.create(pow);

                if(!getEnv().isNumericalEval()) {
                    if (real.isWholeNumber())
                        return real.toInteger();
                    else {
                    	// factor out any numbers that are the Nth root of the denominator
						Expr t = new Factor(this);
						ListExpr factors = new Tally(t).eval().asList();
						int denominator = ((RationalNum)num).denominator().intValue();

						IntegerNum multiply = IntegerNum.ONE;

						for(int i = 0; i < factors.size();) {
							int key = factors.get(i).get(0).asInteger().intValue();
							int val = factors.get(i).get(1).asInteger().intValue();

							// Factor it out
							if (val >= denominator) {
								multiply = new Times(multiply, new IntegerNum(key)).eval().asInteger();
								factors.set(i, new ListExpr(new IntegerNum(key), new IntegerNum(val - denominator)));
							} else
								i++;
						}

						if (multiply.isOne())
							return new Power(this, num);

						// Factor out multiples
						//Expr result = new Power(this, num);
						IntegerNum root = IntegerNum.ONE;
						for(int i = 0; i < factors.size(); i++) {
							root = new Times(root, factors.get(i).get(0), factors.get(i).get(1)).eval().asInteger();
						}

						return new Times(multiply, new Power(root, num));
					}
                }

                return real;

			case REAL: {
				//result = RealNum.create(this);
				break;
			}
		}

		return null;
		//throw new InvalidStateException("missing case");
	}	
	
	//IntegerNum Specific Functions
	public boolean IsEven() {
		return (!val().testBit(0));
	}

	public boolean IsOdd() {
		return (val().testBit(0));
	}
	
	public IntegerNum GCD(IntegerNum N) {
		return new IntegerNum(val().gcd(N.val()));
	}
	
	public IntegerNum PowerMod(NumberExpr b, NumberExpr m) {
		//Assuming all integers at this point since MathFunc needs to check that
		BigInteger num = val();
		BigInteger exp = ((IntegerNum)b).val();
		BigInteger mod = ((IntegerNum)m).val();
	
		return new IntegerNum(num.modPow(exp, mod)) ;
	}
	
	public boolean primeQ() {
		return val().isProbablePrime(5);
	}
	
	public IntegerNum factorial() {
		return factorial((int)this.val().longValue()); 
	}
	
	public static IntegerNum factorial(int N) {
		BigInteger result = BigInteger.valueOf(1);
		while(N > 1)
		{
			result = result.multiply( new BigInteger(N + ""));
			N--;
		}

		return new IntegerNum(result);
	}
	
	public IntegerNum mod(IntegerNum N) {
		return new IntegerNum( val().mod(N.val()));
	}

	@Override
	public int compareTo(NumberExpr o) {
		if (o.isInteger()) {
			BigInteger n1 = val();
			BigInteger n2 = o.asInteger().val();
			return n1.compareTo(n2);
		}

		throw new NotImplementedException();
	}
}
