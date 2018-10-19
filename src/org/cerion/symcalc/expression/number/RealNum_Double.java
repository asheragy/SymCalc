package org.cerion.symcalc.expression.number;

import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

class RealNum_Double extends RealNum {

    private double dNumber = 0;

    private RealNum_Double() {
        this.dNumber = 0;
    }

    RealNum_Double(String s) {
        dNumber = Double.parseDouble(s);
    }

    RealNum_Double(IntegerNum n) {
        dNumber = n.toDouble();
    }

    RealNum_Double(RationalNum r) {
        dNumber = r.toDouble();
    }

    RealNum_Double(double n) {
        dNumber = n;
    }

    @Override
    public double toDouble() {
        return dNumber;
    }

    @Override
    public String toString() {
        return "" + dNumber;
    }

    @Override
    public boolean equals(NumberExpr e) {
        if(e.isReal()) {
            if (e instanceof RealNum_BigDecimal)
                throw new NotImplementedException();

            return this.dNumber == e.toDouble();
        }

        return false;
    }

    public RealNum_Double unaryMinus() {
        return new RealNum_Double(0 - dNumber);
    }

    public boolean isZero() {
        return (dNumber == 0.0);
    }

    public boolean isOne() {
        return (dNumber == 1.0);
    }

    public NumberExpr plus(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.getNumType())
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

        return num.plus(this);
    }

    public NumberExpr minus(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.getNumType())
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

        NumberExpr negative = num.unaryMinus();
        return negative.plus(this);
    }

    public NumberExpr times(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.getNumType())
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
        return num.times(this);
    }

    public NumberExpr div(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.getNumType())
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
        return num.times(this);
    }

    public NumberExpr power(NumberExpr num) {
        RealNum_Double result = new RealNum_Double();
        switch (num.getNumType())
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

    @Override
    public boolean isWholeNumber() {
        if(dNumber == Math.floor(dNumber) && !Double.isInfinite(dNumber))
            return true;

        return false;
    }

    @Override
    public IntegerNum toInteger() {
        return new IntegerNum((long)dNumber);
    }

    @NotNull
    @Override
    public NumberType getNumType() {
        return NumberType.REAL;
    }
}
