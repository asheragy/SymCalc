package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.NumberExpr;

public class RealNum_Double extends NumberExpr {

    private double dNumber = 0;

    public int numType() {
        return REAL;
    }

    public double toDouble() {
        return dNumber;
    }

    public RealNum_Double() {
        this.dNumber = 0;
    }

    public RealNum_Double(String s) {
        dNumber = Double.parseDouble(s);
    }

    public RealNum_Double(IntegerNum n) {
        dNumber = n.toDouble();
    }

    public RealNum_Double(RationalNum r) {
        dNumber = r.toDouble();
    }

    public RealNum_Double(double n) {
        dNumber = n;
    }

    @Override
    public String toString() {
        return "" + dNumber;
    }

    @Override
    public boolean equals(NumberExpr e) {
        if(e.isReal()) {
            RealNum n = (RealNum)e;

            // TODO fix this
            //if(n.bigNumber == null && bigNumber == null)
            //    return n.dNumber == dNumber;
        }

        return false;
    }

    public RealNum_Double negate() {
        return new RealNum_Double(0 - dNumber);
    }

    public boolean isZero() {
        return (dNumber == 0.0);
    }

    public boolean isOne() {
        return (dNumber == 1.0);
    }

    public NumberExpr add(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.numType())
        {
            case INTEGER: //RealNum + IntegerNum
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
                result.dNumber = dNumber + ((RealNum_Double)num).dNumber;
                return result;
            }

        }

        return num.add(this);
    }

    public NumberExpr subtract(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.numType())
        {
            case INTEGER: //RealNum - IntegerNum
            {
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
                result.dNumber = dNumber - ((RealNum_Double)num).dNumber;
                return result;
            }
        }

        NumberExpr negative = num.negate();
        return negative.add(this);
    }

    public NumberExpr multiply(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.numType())
        {
            case INTEGER: //RealNum * IntegerNum
                result.dNumber = dNumber * num.toDouble();
                return result;

            case RATIONAL: //RealNum * RationalNum
            {
                result.dNumber = dNumber * num.toDouble();
                return result;
            }

            case REAL: //RealNum * RealNum
            {
                result.dNumber = dNumber * ((RealNum_Double)num).dNumber;
                return result;
            }
        }
        return num.multiply(this);
    }

    public NumberExpr divide(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.numType())
        {
            case INTEGER: //RealNum / IntegerNum
                result.dNumber = dNumber / num.toDouble();
                return result;

            case RATIONAL: //RealNum / RationalNum
            {
                result.dNumber = dNumber / num.toDouble();
                return result;
            }

            case REAL: //RealNum / RealNum
            {
                result.dNumber = dNumber / ((RealNum_Double)num).dNumber;
                return result;
            }
        }
        return num.multiply(this);
    }

    public boolean canExp(NumberExpr num) {
        if(num.numType() == COMPLEX)
            return false;
        return true;
    }

    public NumberExpr power(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
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
    }

}
