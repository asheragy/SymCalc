package org.cerion.symcalc.expression.number

internal class RealNum_Double(value: Double = 0.0) : RealNum() {
    override val precision: Int
        get() = 0

    private var dNumber: Double = value

    override val isZero: Boolean get() = dNumber == 0.0
    override val isOne: Boolean get() = dNumber == 1.0
    override val isNegative: Boolean get() = dNumber < 0

    override val isWholeNumber: Boolean
        get() = if (dNumber == Math.floor(dNumber) && !java.lang.Double.isInfinite(dNumber)) true else false

    constructor(n: IntegerNum) : this() { dNumber = n.toDouble() }

    override fun toDouble(): Double = dNumber
    override fun toString(): String = "" + dNumber

    override fun compareTo(other: NumberExpr): Int {
        return toDouble().compareTo(other.toDouble())
    }

    /*
    override fun equals(e: NumberExpr): Boolean {
        if (e.isReal) {
            if (e is RealNum_BigDecimal)
                throw NotImplementedError()

            return this.dNumber == e.toDouble()
        }

        return false
    }
    */

    override fun unaryMinus(): RealNum_Double = RealNum_Double(0 - dNumber)

    override fun plus(other: NumberExpr): NumberExpr {
        val result = RealNum_Double()
        when (other.numType) {
            NumberType.INTEGER -> {
                result.dNumber = dNumber + other.toDouble()
                return result
            }

            NumberType.RATIONAL -> {
                result.dNumber = dNumber + other.toDouble()
                return result
            }

            NumberType.REAL -> {
                result.dNumber = dNumber + other.asReal().toDouble()
                return result
            }
            else -> {
                return other + this
            }
        }
    }

    override fun minus(other: NumberExpr): NumberExpr {
        val result = RealNum_Double()
        when (other.numType) {
            NumberType.INTEGER -> {
                result.dNumber = dNumber - other.toDouble()
                return result
            }
            NumberType.RATIONAL -> {
                result.dNumber = dNumber - other.toDouble()
                return result
            }
            NumberType.REAL -> {
                result.dNumber = dNumber - (other as RealNum_Double).dNumber
                return result
            }
            else -> {
                return other - this
            }
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        val result = RealNum_Double()
        when (other.numType) {
            NumberType.INTEGER -> {
                result.dNumber = dNumber * other.toDouble()
                return result
            }

            NumberType.RATIONAL -> {
                result.dNumber = dNumber * other.toDouble()
                return result
            }

            NumberType.REAL -> {
                result.dNumber = dNumber * (other as RealNum_Double).dNumber
                return result
            }
            else -> {
                return other * this
            }
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        val result = RealNum_Double()
        when (other.numType) {
            NumberType.INTEGER -> {
                result.dNumber = dNumber / other.toDouble()
                return result
            }
            NumberType.RATIONAL -> {
                result.dNumber = dNumber / other.toDouble()
                return result
            }
            NumberType.REAL -> {
                result.dNumber = dNumber / (other as RealNum_Double).dNumber
                return result
            }
            else -> {
                throw NotImplementedError()
            }
        }
    }

    override fun power(other: NumberExpr): NumberExpr {
        val result = RealNum_Double()
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL,
            NumberType.REAL
            -> {
                result.dNumber = Math.pow(dNumber, other.toDouble())
                return result
            }
            else -> {
                throw NotImplementedError()
            }
        }
    }

    override fun toInteger(): IntegerNum {
        return IntegerNum(dNumber.toLong())
    }
}
