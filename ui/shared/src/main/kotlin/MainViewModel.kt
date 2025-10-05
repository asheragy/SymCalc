
import androidx.compose.runtime.*
import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec

private const val EXPR_PLACEHOLDER = "<EXPR>"

class MainViewModel(initialDisplay: String = "") {
    private var _input by mutableStateOf<String>(initialDisplay)

    val display = derivedStateOf {
        val it = _input
        if (it.startsWith(EXPR_PLACEHOLDER)) {
            val remaining = it.replace(EXPR_PLACEHOLDER, "")
            exprToString(lastExpr!!) + remaining
        }
        else
            it
    }

    private var _preview by mutableStateOf("")
    val preview: State<String> = derivedStateOf {
        _preview
    }

    private var lastExpr: Expr? = null

    init {
        if (initialDisplay.isNotEmpty())
            _preview = Expr.parse(initialDisplay).eval().toString()
    }

    fun clear() {
        _input = ""
        _preview = ""
    }

    // TODO auto closing bracket "(3+1 = 4"

    fun onKey(key: Key) {

        when(key) {
            Key.DEL -> {
                if (preview.value!!.isEmpty())
                    _input = ""
                if (_input.isNotEmpty())
                    _input = _input.substring(0, _input.length - 1)
            }
            Key.CLEAR -> {
                _input = ""
                _preview = ""
            }
            Key.EVAL -> {
                // TODO fix precision 8.05 - 5
                val result = eval()

                if (result.isError)
                    _preview = result.toString()
                else {
                    lastExpr = result
                    _input = EXPR_PLACEHOLDER
                    _preview = ""
                }
            }
            Key.NOOP -> {
                // Nothing
            }
            else -> {
                _input = _input + key.inputValue()
                println("updated = " + _input)
            }
        }

        if (key != Key.EVAL) {
            val inputEval = eval()
            if (!inputEval.isError) {
                val previewStr = exprToString(inputEval)
                if (previewStr != _input)
                    _preview = exprToString(inputEval)
            }
        }
    }

    fun directInput(input: String) {
        onKey(Key.CLEAR)
        _input = _input!! + input
        onKey(Key.NOOP)
    }

    private fun exprToString(expr: Expr): String {
        val e = when(expr) {
            is ErrorExpr -> expr
            is Integer -> expr
            else -> expr.eval(12)
        }

        return if (e is RealBigDec)
            e.toString().split("`")[0]
        else
            e.toString()
    }

    private fun eval(): Expr {
        val input = _input!!

        val inputExpr = if (input.startsWith(EXPR_PLACEHOLDER)) {
            val remaining = input.replace(EXPR_PLACEHOLDER, "")
            Expr.parse(lastExpr!!, remaining)
        }
        else {
            Expr.parse(input)
        }

        return inputExpr.eval()
    }
}