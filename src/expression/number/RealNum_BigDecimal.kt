package org.cerion.symcalc.expression.number

import org.cerion.symcalc.Environment
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.min

class RealNum_BigDecimal(override val value: BigDecimal) : RealNum() {

    private val bigNumber: BigDecimal
        get() = value

    constructor(value: String) : this(BigDecimal(value))

    init {
        setNumericalEval(true, value.scale())
    }

    //override val precision: Int
    //    get() = bigNumber.scale()

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
        if (isNumericalEval) {
            if(precision == SYSTEM_DECIMAL_PRECISION)
                return RealNum.create(toDouble())
            else if (precision < bigNumber.scale())
                return RealNum.create(bigNumber.setScale(precision, RoundingMode.HALF_UP))
            else
                setNumericalEval(true, bigNumber.scale())
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
                rational.setNumericalEval(true, precision)

                return this + rational.eval().asReal()
            }

            NumberType.REAL -> {
                val real = other.asReal()
                if(real.isDouble)
                    return real + this

                real as RealNum_BigDecimal

                // Both are BigDecimal
                val result = RealNum_BigDecimal( this.bigNumber.plus(real.bigNumber))
                return evaluatePrecision(result, this, real)
            }

            else -> {
                return other + this
            }
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        when(other.numType) {
            NumberType.INTEGER -> TODO()
            NumberType.RATIONAL -> TODO()
            NumberType.REAL -> {
                val real = other.asReal()
                if(real.isDouble)
                    return real * this

                real as RealNum_BigDecimal

                // Both are BigDecimal
                val result = RealNum_BigDecimal( this.bigNumber.times(real.bigNumber))
                return evaluatePrecision(result, this, real)
            }
            NumberType.COMPLEX -> TODO()
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER -> TODO()
            NumberType.RATIONAL -> TODO()
            NumberType.REAL -> {
                val real = other.asReal()
                if(real.isDouble)
                    return real / this

                real as RealNum_BigDecimal

                // Both are BigDecimal
                val result = RealNum_BigDecimal( this.bigNumber.divide(real.bigNumber, RoundingMode.HALF_UP))
                return evaluatePrecision(result, this, real)
            }
            NumberType.COMPLEX -> TODO()
        }
    }

    override fun power(other: NumberExpr): NumberExpr {

        // Special case square root
        if(other.isRational && other.equals(RationalNum(1,2)))
            return RealNum_BigDecimal(bigNumber.sqrt(MathContext.DECIMAL32))

        when (other.numType) {
            NumberType.INTEGER -> {
                val number = bigNumber.pow(other.asInteger().intValue())
                return RealNum_BigDecimal(number)
            }
            NumberType.RATIONAL -> TODO()
            NumberType.REAL -> TODO()
            NumberType.COMPLEX -> TODO()
        }
    }

    private fun evaluatePrecision(result: RealNum_BigDecimal, a: RealNum_BigDecimal, b: RealNum_BigDecimal): RealNum_BigDecimal {
        // TODO maybe set scale on environment and call evaluate()
        val min = min(a.bigNumber.scale(), b.bigNumber.scale())
        return RealNum_BigDecimal(result.bigNumber.setScale(min, RoundingMode.HALF_UP))
    }
}
