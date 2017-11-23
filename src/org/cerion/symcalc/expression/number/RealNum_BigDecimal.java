package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.NumberExpr;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;

public class RealNum_BigDecimal extends NumberExpr {

    private BigDecimal bigNumber = null;

    public int numType() {
        return REAL;
    }

    public double toDouble() {
        return bigNumber.doubleValue();
    }

    public RealNum_BigDecimal() {
        bigNumber = new BigDecimal("0");
    }

    public RealNum_BigDecimal(String s) {
        bigNumber = new BigDecimal(s);
    }

    public RealNum_BigDecimal(IntegerNum n) {
        bigNumber = new BigDecimal(n.toBigInteger());
    }

    public RealNum_BigDecimal (RationalNum r) {
        throw new NotImplementedException();
    }

    public RealNum_BigDecimal(double n) {
        bigNumber = new BigDecimal(n);
    }

    public RealNum_BigDecimal(BigDecimal n) {
        bigNumber = n;
    }

    @Override
    public String toString() {
        return bigNumber.toString();
    }

    @Override
    public boolean equals(NumberExpr e) {
        if(e.isReal()) {
            //RealNum n = (RealNum)e;
            return toDouble() == e.toDouble();
        }

        return false;
    }

    public RealNum_BigDecimal negate() {
        return new RealNum_BigDecimal(bigNumber.negate());
    }

    public boolean isZero() {
        return bigNumber.equals(BigDecimal.ZERO);
    }

    public boolean isOne() {
            return bigNumber.equals(BigDecimal.ONE);
    }

    public NumberExpr add(NumberExpr num) {
        throw new NotImplementedException();
        /*
        RealNum result = new RealNum();
        switch (num.numType())
        {
            case INTEGER: //RealNum + IntegerNum

                //result.bigNumber = new BigDecimal( ((IntegerNum)num).integer );
                //result.bigNumber = result.bigNumber.add(this.bigNumber);
                result.dNumber = dNumber + num.toDouble();
                return result;

            case RATIONAL: //RealNum + RationalNum
            {
                result.dNumber = dNumber + num.toDouble();
                return result;
            }

            case REAL: //RealNum + RealNum
            {
                //result.bigNumber = this.bigNumber.add( ((RealNum)num).bigNumber );
                result.dNumber = dNumber + ((RealNum)num).dNumber;
                return result;
            }

        }

        return num.add(this);
        */
    }

    public NumberExpr subtract(NumberExpr num) {
        throw new NotImplementedException();
        /*
        RealNum result = new RealNum();
        switch (num.numType())
        {
            case INTEGER: //RealNum - IntegerNum
            {
                //result.bigNumber = new BigDecimal( ((IntegerNum)num).integer );
                //result.bigNumber = bigNumber.subtract(result.bigNumber);
                result.dNumber = dNumber - num.toDouble();
                return result;
            }

            case RATIONAL: //RealNum - RationalNum
            {
                result.dNumber = dNumber - num.toDouble();
                return result;
            }

            case REAL: //RealNum - RealNum
            {
                //result.bigNumber = this.bigNumber.subtract( ((RealNum)num).bigNumber );
                result.dNumber = dNumber - ((RealNum)num).dNumber;
                return result;
            }
        }

        NumberExpr negative = num.negate();
        return negative.add(this);
        */
    }

    public NumberExpr multiply(NumberExpr num) {
        throw new NotImplementedException();
        /*
        RealNum result = new RealNum();
        switch (num.numType())
        {
            case INTEGER: //RealNum * IntegerNum

                //result.bigNumber = new BigDecimal( ((IntegerNum)num).integer );
                //result.bigNumber = result.bigNumber.multiply(this.bigNumber);
                result.dNumber = dNumber * num.toDouble();
                return result;

            case RATIONAL: //RealNum * RationalNum
            {
                result.dNumber = dNumber * num.toDouble();
                return result;
            }

            case REAL: //RealNum * RealNum
            {
                //result.bigNumber = this.bigNumber.multiply( ((RealNum)num).bigNumber );
                result.dNumber = dNumber * ((RealNum)num).dNumber;
                return result;
            }
        }
        return num.multiply(this);
        */
    }

    public NumberExpr divide(NumberExpr num) {
        throw new NotImplementedException();
        /*
        RealNum result = new RealNum();
        switch (num.numType())
        {
            case INTEGER: //RealNum / IntegerNum

                //result.bigNumber = new BigDecimal( ((IntegerNum)num).integer );
                //result.bigNumber = result.bigNumber.multiply(this.bigNumber);
                result.dNumber = dNumber / num.toDouble();
                return result;

            case RATIONAL: //RealNum / RationalNum
            {
                result.dNumber = dNumber / num.toDouble();
                return result;
            }

            case REAL: //RealNum / RealNum
            {
                //result.bigNumber = this.bigNumber.multiply( ((RealNum)num).bigNumber );
                result.dNumber = dNumber / ((RealNum)num).dNumber;
                return result;
            }
        }
        return num.multiply(this);
        */
    }

    public boolean canExp(NumberExpr num) {
        if(num.numType() == COMPLEX)
            return false;
        return true;
    }

    public NumberExpr power(NumberExpr num) {
        throw new NotImplementedException();
        /*
        RealNum result = new RealNum();
        switch (num.numType())
        {
            case INTEGER: //RealNum ^ IntegerNum
            case RATIONAL: //RealNum ^ RationalNum
            case REAL: //RealNum ^ RealNum
            {
                result.dNumber = Math.pow( dNumber, num.toDouble());
                return result;
            }
        }

        return null;
        */
    }

}