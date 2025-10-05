
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

val ButtonColor = Color(0xFF111111)

@Composable
fun KeyPad(onKeyPress: (key: Key) -> Unit) {
    Column {
        Row(Modifier.weight(0.3f)) {
            KeyPadExt(onKeyPress = onKeyPress)
        }
        Row(Modifier.weight(0.7f)) {
            KeyPadBasic(onKeyPress = onKeyPress)
        }
    }
}

/*
    Sqrt    ^2  ^   !
    Pi      E   Ln  Log
    Sin     Cos Tan debug

    AC      (   )   /
    7       8   9   x
    4       5   6   -
    1       2   3   +
    0       .   DEL =
 */
@Composable
fun KeyPadBasic(onKeyPress: (key: Key) -> Unit) {
    Column {
        Row(Modifier.weight(1.0f)) {
            Key("AC") { onKeyPress(Key.CLEAR) }
            Key("(") { onKeyPress(Key.BRACKET_LEFT) }
            Key(")") { onKeyPress(Key.BRACKET_RIGHT) }
            Key("/") { onKeyPress(Key.DIVIDE) }
        }
        Row(Modifier.weight(1.0f)) {
            Key("7") { onKeyPress(Key.NUM_7) }
            Key("8") { onKeyPress(Key.NUM_8) }
            Key("9") { onKeyPress(Key.NUM_9) }
            Key("*") { onKeyPress(Key.TIMES) }
        }
        Row(Modifier.weight(1.0f)) {
            Key("4") { onKeyPress(Key.NUM_4) }
            Key("5") { onKeyPress(Key.NUM_5) }
            Key("6") { onKeyPress(Key.NUM_6) }
            Key("-") { onKeyPress(Key.MINUS) }
        }
        Row(Modifier.weight(1.0f)) {
            Key("1") { onKeyPress(Key.NUM_1) }
            Key("2") { onKeyPress(Key.NUM_2) }
            Key("3") { onKeyPress(Key.NUM_3) }
            Key("+") { onKeyPress(Key.PLUS) }
        }
        Row(Modifier.weight(1.0f)) {
            Key("0") { onKeyPress(Key.NUM_0) }
            Key(".") { onKeyPress(Key.DOT) }
            Key("DEL") { onKeyPress(Key.DEL) }
            Key("=") { onKeyPress(Key.EVAL) }
        }
    }
}

//Sqrt    ^2  ^   !
//Pi      E   Ln  Log

@Composable
fun KeyPadExt(onKeyPress: (key: Key) -> Unit) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .background(ButtonColor)
                .weight(0.25f)) {
            Key("Sqrt") { onKeyPress(Key.SQRT) }
            Key("^2") { onKeyPress(Key.SQUARE) }
            Key("^") { onKeyPress(Key.POW) }
            Key("!") { onKeyPress(Key.FACTORIAL) }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(ButtonColor)
                .weight(0.25f)) {
            Key("Pi") { onKeyPress(Key.PI) }
            Key("E") { onKeyPress(Key.E) }
            Key("Ln") { onKeyPress(Key.LN) }
            Key("Log") { onKeyPress(Key.LOG) }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(ButtonColor)
                .weight(0.25f)) {
            Key("Sin") { onKeyPress(Key.SIN) }
            Key("Cos") { onKeyPress(Key.COS) }
            Key("Tan") { onKeyPress(Key.TAN) }
            Key("DEBUG") { onKeyPress(Key.DEBUG) }
        }
    }
}

@Composable
fun ColumnScope.Key(label: String, onKeyPress:() -> Unit) {
    Button(onClick = { onKeyPress() },
        Modifier
            .fillMaxWidth()
            .weight(1f),
        colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor))
    {
        Text(text = label, color = Color.White)
    }
}

@Composable
fun RowScope.Key(label: String, onKeyPress:() -> Unit) {
    Button(onClick = { onKeyPress() },
        Modifier
            .fillMaxHeight()
            .weight(1f),
        colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor))
    {
        Text(text = label, color = Color.White)
    }
}
