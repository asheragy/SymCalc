package org.cerion.symcalc.expression.number

import java.math.BigDecimal

class RealNum_BigDecimal(value: BigDecimal) : RealNum() {

    private var bigNumber: BigDecimal = value

    override val isWholeNumber: Boolean
        get() = throw NotImplementedError()

    override val isZero: Boolean get() = bigNumber == BigDecimal.ZERO
    override val isOne: Boolean get() = bigNumber == BigDecimal.ONE
    override val isNegative: Boolean get() = bigNumber.signum() == -1

    override fun toInteger(): IntegerNum {
        return IntegerNum(bigNumber.toLong())
    }

    override fun toDouble(): Double {
        return bigNumber.toDouble()
    }

    override fun compareTo(other: NumberExpr): Int {
        return toDouble().compareTo(other.toDouble())
    }

    /*
    constructor() {
        super(BigDecimal("0")
    }
    */

    /*
    constructor(s: String) {
        bigNumber = BigDecimal(s)
    }

    constructor(n: IntegerNum) {
        bigNumber = BigDecimal(n.toBigInteger())
    }

    constructor(r: RationalNum) {
        throw NotImplementedException()
    }

    constructor(n: Double) {
        bigNumber = BigDecimal(n)
    }
    */

    override fun toString(): String {
        return bigNumber.toString()
    }

    /*
    override fun equals(e: NumberExpr): Boolean {
        return e.isReal && toDouble() == e.toDouble()
    }
    */

    override fun unaryMinus(): RealNum_BigDecimal {
        return RealNum_BigDecimal(bigNumber.negate())
    }

    override fun plus(other: NumberExpr): NumberExpr {
        throw NotImplementedError()
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

    override fun minus(other: NumberExpr): NumberExpr {
        throw NotImplementedError()
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

    override fun times(other: NumberExpr): NumberExpr {
        throw NotImplementedError()
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

    override fun div(other: NumberExpr): NumberExpr {
        throw NotImplementedError()
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

    override fun power(other: NumberExpr): NumberExpr {
        throw NotImplementedError()
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
