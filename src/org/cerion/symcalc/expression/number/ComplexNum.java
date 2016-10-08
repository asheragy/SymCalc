package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.NumberExpr;

public class ComplexNum extends NumberExpr
{
	public int numType() {
		return COMPLEX;
	}

	public static final ComplexNum ZERO = new ComplexNum();

	public ComplexNum() {
		setArgs(IntegerNum.ZERO, IntegerNum.ZERO);
	}
	
	public ComplexNum(NumberExpr r, NumberExpr i) {
		setArgs(r,i);
	}

	public ComplexNum(int r, int i) {
		this(new IntegerNum(r), new IntegerNum(i));
	}
	
	public ComplexNum(String s) {
		String num = s.substring(0, s.length()-1);
		//System.out.println(num);
		setArgs(IntegerNum.ZERO);

		if(num.length() == 0) //If passed just "i" its complex number 1
			setArgs(IntegerNum.ONE);
		else
			setArgs(parse(num));
	}

	@Override
	public String toString() {
		return "ComplexNum[" + getReal().toString() + "," + getImg().toString() + "]";
	}

	@Override
	public ComplexNum negate() {
		return new ComplexNum(getReal().negate(), getImg().negate());
	}

	@Override
	public double toDouble() {
		System.out.println("Error ComplexNum.toDouble()");
		return 0;
	}

	@Override
	public boolean equals(NumberExpr e) {
		if(e.isComplex()) {
			ComplexNum c = (ComplexNum)e;
			return getReal().equals(c.getReal()) && getImg().equals(c.getImg());
		} else if(getImg().isZero()) {
			//Non complex number can be equal as long as the imaginary part is zero
			return getReal().equals(e);
		}

		return false;
	}

	public NumberExpr getReal()
	{
		return (NumberExpr) get(0);
	}
	
	public NumberExpr getImg()
	{
		return (NumberExpr) get(1);
	}
	
	private void setReal(NumberExpr n) {
		setArg(0, n);
	}
	
	private void setImg(NumberExpr n) {
		setArg(1, n);
	}
	
	public boolean isZero() {
		return (getReal().isZero() && getImg().isZero());
	}
	
	public boolean isOne()
	{
		return (getReal().isOne() && getImg().isZero());
	}
	
	public NumberExpr add(NumberExpr num)
	{
		if(num.numType() == NumberExpr.COMPLEX)
		{
			ComplexNum b = (ComplexNum)num;
			return new ComplexNum( getReal().add(b.getReal()), getImg().add(b.getImg()));
			
		}
		
		return new ComplexNum( getReal().add(num), getImg());
	}
	
	public NumberExpr subtract(NumberExpr num) 
	{
		ComplexNum result = new ComplexNum();
		if(num.numType() == NumberExpr.COMPLEX)
		{
			ComplexNum b = (ComplexNum)num;
			result.setReal (getReal().subtract(b.getReal()));
			result.setImg (getImg().subtract(b.getImg()));
			
		}
		else
		{
			result.setReal (getReal().subtract(num));
			result.setImg( getImg() );
		}
		
		return result;
	}
	
	public NumberExpr multiply(NumberExpr num) 
	{
		ComplexNum result = new ComplexNum();
		switch (num.numType()) 
		{
			case NumberExpr.COMPLEX: //ComplexNum + ComplexNum
			{
			    //(a+bi)(c+di) = (ac-bd) + (bc+ad)i
				NumberExpr a = this.getReal();
				NumberExpr b = this.getImg();
				NumberExpr c = ((ComplexNum)num).getReal();
				NumberExpr d = ((ComplexNum)num).getImg();
				
				NumberExpr temp1 = a.multiply(c);
				NumberExpr temp2 = b.multiply(d);
				result.setReal (temp1.subtract(temp2));

				temp1 = b.multiply(c);
				temp2 = a.multiply(d);
				result.setImg( temp1.add(temp2));
				break;
			}
			default: 
			{
				result.setReal( this.getReal().multiply(num));
				result.setImg( this.getImg().multiply(num));
				break;
			}
		}
		
		return result;
	}
	
	@Override
	public NumberExpr divide(NumberExpr num) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canExp(NumberExpr num)
	{
		return false;
	}
	
	public NumberExpr power(NumberExpr num) 
	{
		ComplexNum result = new ComplexNum();
		
		switch (num.numType()) 
		{
			case 0: //ComplexNum ^ IntegerNum
			{
				
				
				break;
			}
				
			/*
			case 1:
			{
				result.img = this.img.multiply(num);
				result.real = this.real.multiply(num);
				break;
			}
			*/
			
			//case 1: //ComplexNum + RealNum
			//{
			//	result.real = this.real.add(num);
			//	result.img = this.img;
			//	break;
			//}

			/*
			case 2: //ComplexNum + ComplexNum
			{
			    //(a+bi)(c+di) = (ac-bd) + (bc+ad)i
				Number a = this.real;
				Number b = this.img;
				Number c = ((ComplexNum)num).real;
				Number d = ((ComplexNum)num).img;
				
				Number temp1 = a.multiply(c);
				Number temp2 = b.multiply(d);
				result.real = temp1.subtract(temp2);

				temp1 = b.multiply(c);
				temp2 = a.multiply(d);
				result.img = temp1.add(temp2);
				
				break;
			}
			*/
			//default : System.out.println("ComplexNum.multiply() no case"); return getNumber("99.9");
		}
		
		//System.out.println(this + " * " + num + " = " + result);
		return result;
	}	

}