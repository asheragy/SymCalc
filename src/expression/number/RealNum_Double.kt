package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.arithmetic.Power
import kotlin.math.floor
import kotlin.math.pow

internal class RealNum_Double(value: Double = 0.0) : RealNum() {

    private var dNumber: Double = value

    override val isZero: Boolean get() = dNumber == 0.0
    override val isOne: Boolean get() = dNumber == 1.0
    override val isNegative: Boolean get() = dNumber < 0

    override fun toInteger(): IntegerNum = IntegerNum(dNumber.toLong())
    override val isWholeNumber: Boolean
        get() = dNumber == floor(dNumber) && !java.lang.Double.isInfinite(dNumber)

    constructor(n: IntegerNum) : this() { dNumber = n.toDouble() }

    init {
        setNumericalEval(true, SYSTEM_DECIMAL_PRECISION)
    }

    override fun toDouble(): Double = dNumber
    override fun toString(): String = "" + dNumber

    override fun compareTo(other: NumberExpr): Int {
        when(other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL,
            NumberType.REAL -> return toDouble().compareTo(other.toDouble())
            NumberType.COMPLEX -> return ComplexNum(this).compareTo(other)
        }
    }

    override fun unaryMinus(): RealNum_Double = RealNum_Double(0 - dNumber)

    override fun plus(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL,
            NumberType.REAL -> return create(dNumber + other.toDouble())
            NumberType.COMPLEX -> return ComplexNum(this) + other
        }
    }

    override fun minus(other: NumberExpr): NumberExpr {
        return this + other.unaryMinus()
    }

    override fun times(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL,
            NumberType.REAL -> return RealNum_Double(dNumber * other.toDouble())
            NumberType.COMPLEX -> return ComplexNum(this) * other
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL,
            NumberType.REAL -> return RealNum_Double(dNumber / other.toDouble())
            NumberType.COMPLEX -> return ComplexNum(this) / other
        }
    }

    override fun power(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL,
            NumberType.REAL -> return create(dNumber.pow(other.toDouble()))
            NumberType.COMPLEX -> return Power(this, other).eval() as NumberExpr
        }
    }
}
