package org.cerion.symcalc.expression.number

import org.cerion.symcalc.Environment
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class RealNum_BigDecimal(bigDecimal: BigDecimal) : RealNum() {

    private val bigNumber: BigDecimal
        get() = value as BigDecimal

    init {
        this.value = bigDecimal
    }

    override val precision: Int
        get() = bigNumber.scale()

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

    override fun toString(): String {
        return bigNumber.toString()
    }

    override fun compareTo(other: NumberExpr): Int {
        if (other.isReal && !other.asReal().isDouble) {
            val bigDec = other.asReal() as RealNum_BigDecimal
            return bigNumber.compareTo(bigDec.bigNumber)
        }

        return toDouble().compareTo(other.toDouble())
    }

    override fun evaluate(): NumberExpr {
        if (env.isNumericalEval) {
            if(env.precision == Environment.SYSTEM_DECIMAL_PRECISION)
                return RealNum.create(toDouble())
            else if (env.precision < precision)
                return RealNum.create(bigNumber.setScale(env.precision, RoundingMode.HALF_UP))
        }

        return this
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

        when (other.numType) {
            NumberType.INTEGER -> {
                val bigDec = other.asInteger().toBigDecimal()
                return RealNum_BigDecimal( this.bigNumber.plus(bigDec))
            }

            NumberType.RATIONAL -> {
                val rational = other.asRational()
                rational.env.setNumericalEval(true, precision)

                return this + rational.eval().asReal()
            }

            NumberType.REAL -> {
                val real = other.asReal()
                if(real.isDouble)
                    return real + this

                // Both are BigDecimal
                return RealNum_BigDecimal( this.bigNumber.plus((real as RealNum_BigDecimal).bigNumber))
            }
            else -> {
                return other + this
            }
        }
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

        // Special case square root
        if(other.isRational && other.equals(RationalNum(1,2)))
            return RealNum_BigDecimal(bigNumber.sqrt(MathContext.DECIMAL32))

        when (other.numType)
        {
            NumberType.INTEGER -> {
                val number = bigNumber.pow(other.asInteger().intValue())
                return RealNum_BigDecimal(number)
            }
            //case RATIONAL: //RealNum ^ RationalNum

        }

        throw NotImplementedError()
    }
}
