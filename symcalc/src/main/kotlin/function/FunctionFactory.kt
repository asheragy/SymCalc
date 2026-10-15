package org.cerion.symcalc.function

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.function.calculus.D
import org.cerion.symcalc.function.calculus.Sum
import org.cerion.symcalc.function.combinatorial.Binomial
import org.cerion.symcalc.function.combinatorial.Factorial
import org.cerion.symcalc.function.combinatorial.Factorial2
import org.cerion.symcalc.function.core.*
import org.cerion.symcalc.function.core.Set
import org.cerion.symcalc.function.hyperbolic.*
import org.cerion.symcalc.function.integer.*
import org.cerion.symcalc.function.list.*
import org.cerion.symcalc.function.list.Map
import org.cerion.symcalc.function.logical.Equal
import org.cerion.symcalc.function.logical.Greater
import org.cerion.symcalc.function.matrix.Dot
import org.cerion.symcalc.function.matrix.IdentityMatrix
import org.cerion.symcalc.function.matrix.MatrixQ
import org.cerion.symcalc.function.matrix.VectorQ
import org.cerion.symcalc.function.numeric.Floor
import org.cerion.symcalc.function.procedural.If
import org.cerion.symcalc.function.special.Gamma
import org.cerion.symcalc.function.special.Pochhammer
import org.cerion.symcalc.function.special.PolyGamma
import org.cerion.symcalc.function.special.Zeta
import org.cerion.symcalc.function.statistics.*
import org.cerion.symcalc.function.trig.*

object FunctionFactory {

    // kotlin.collections.Map is shadowed by the imported list.Map function class
    private val functions: kotlin.collections.Map<String, (Array<out Expr>) -> FunctionExpr> = mapOf(
        "N" to { N(*it) },
        "Hold" to { Hold(*it) },
        "NumberQ" to { NumberQ(*it) },
        "NumericQ" to { NumericQ(*it) },
        "Equal" to { Equal(*it) },

        "Plus" to { Plus(*it) },
        "Subtract" to { Subtract(*it) },
        "Times" to { Times(*it) },
        "Divide" to { Divide(it[0], it[1]) },
        "Power" to { Power(*it) },
        "Minus" to { Minus(*it) },
        "Log" to { Log(*it) },
        "Log10" to { Log10(*it) },
        "Exp" to { Exp(*it) },
        "Sqrt" to { Sqrt(*it) },

        // Trig
        "Sin" to { Sin(it[0]) },
        "Cos" to { Cos(it[0]) },
        "Tan" to { Tan(it[0]) },
        "Sec" to { Sec(it[0]) },
        "Csc" to { Csc(it[0]) },
        "Cot" to { Cot(it[0]) },
        "ArcSin" to { ArcSin(it[0]) },
        "ArcCos" to { ArcCos(it[0]) },
        "ArcTan" to { ArcTan(*it) },
        "ArcSec" to { ArcSec(it[0]) },
        "ArcCsc" to { ArcCsc(it[0]) },
        "ArcCot" to { ArcCot(it[0]) },

        // Hyperbolic
        "Sinh" to { Sinh(it[0]) },
        "Cosh" to { Cosh(it[0]) },
        "Tanh" to { Tanh(it[0]) },
        "Sech" to { Sech(it[0]) },
        "Csch" to { Csch(it[0]) },
        "Coth" to { Coth(it[0]) },
        "ArcSinh" to { ArcSinh(it[0]) },
        "ArcCosh" to { ArcCosh(it[0]) },
        "ArcTanh" to { ArcTanh(it[0]) },
        "ArcSech" to { ArcSech(it[0]) },
        "ArcCsch" to { ArcCsch(it[0]) },
        "ArcCoth" to { ArcCoth(it[0]) },

        // List
        "Total" to { Total(*it) },
        "Range" to { Range(*it) },
        "Reverse" to { Reverse(*it) },
        "First" to { First(*it) },
        "Last" to { Last(*it) },
        "Table" to { Table(*it) },
        "Flatten" to { Flatten(*it) },
        "Partition" to { Partition(*it) },
        "Join" to { Join(*it) },
        "Select" to { Select(*it) },
        "ConstantArray" to { ConstantArray(*it) },
        "Tally" to { Tally(*it) },
        "Map" to { Map(*it) },

        // Matrix
        "VectorQ" to { VectorQ(*it) },
        "MatrixQ" to { MatrixQ(*it) },
        "IdentityMatrix" to { IdentityMatrix(*it) },
        "Dot" to { Dot(*it) },

        // Integer
        "Mod" to { Mod(*it) },
        "PowerMod" to { PowerMod(*it) },
        "GCD" to { GCD(*it) },
        "Fourier" to { Fourier(*it) },
        "PrimeQ" to { PrimeQ(*it) },
        "Fibonacci" to { Fibonacci(*it) },
        "Factor" to { Factor(*it) },
        "Bernoulli" to { Bernoulli(*it) },
        "IntegerDigits" to { IntegerDigits(*it) },
        "EvenQ" to { EvenQ(*it) },
        "OddQ" to { OddQ(*it) },
        "EulerPhi" to { EulerPhi(*it) },
        "EulerE" to { EulerE(*it) },
        "Divisors" to { Divisors(*it) },
        "DivisorSigma" to { DivisorSigma(*it) },

        // Combinatorial
        "Factorial" to { Factorial(*it) },
        "Factorial2" to { Factorial2(*it) },
        "Binomial" to { Binomial(*it) },

        // Numeric
        "Floor" to { Floor(*it) },

        // Calculus
        "D" to { D(*it) },
        "Sum" to { Sum(*it) },

        // Logical
        "Greater" to { Greater(*it) },

        // Statistics
        "Mean" to { Mean(*it) },
        "Variance" to { Variance(*it) },
        "StandardDeviation" to { StandardDeviation(*it) },
        "RandomChoice" to { RandomChoice(*it) },
        "RandomInteger" to { RandomInteger(*it) },

        // Categorize these
        "Set" to { Set(*it) },
        "CompoundExpression" to { CompoundExpression(*it) },

        "If" to { If(*it) },

        // Special
        "Gamma" to { Gamma(*it) },
        "Pochhammer" to { Pochhammer(*it) },
        "PolyGamma" to { PolyGamma(*it) },
        "Zeta" to { Zeta(*it) }
    )

    fun createInstance(name: String, vararg e: Expr): FunctionExpr {
        val function = functions[name]
        if (function != null) {
            val result = function.invoke(e)
            // TODO unnecessary after all non-vararg functions implement validate()
            require(result.size == e.size) { "expected ${result.size} parameters, actual ${e.size}" }

            return result
        }

        throw IllegalArgumentException("Function ${name} not found")
    }

    @JvmStatic
    fun isValidFunction(functionName: String) = functions.contains(functionName)
}
