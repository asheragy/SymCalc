import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


val viewModel = MainViewModel("2 + 1/2")

@Composable
fun Calculator() {
    val input = viewModel.display
    val mathText = viewModel.mathText
    val preview = viewModel.preview
    var textInput by remember { mutableStateOf(input.value) }

    LaunchedEffect(input.value) {
        if (textInput != input.value) {
            textInput = input.value
        }
    }

    Column(Modifier.background(Color.DarkGray, RectangleShape)) {
        TextField(
            value = textInput,
            onValueChange = {
                textInput = it
                viewModel.onTextInput(it)
            },
            Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 40.sp, textAlign = TextAlign.Right),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = Color.DarkGray,
                cursorColor = Color.White
            )
        )
        LatexView(mathText.value, modifier = Modifier.fillMaxWidth())
        Text(text = preview.value,
            Modifier.fillMaxWidth(),
            fontSize = 30.sp,
            textAlign = TextAlign.Right, color = Color.LightGray)
        KeyPad {
            viewModel.onKey(it)
        }
    }

}
