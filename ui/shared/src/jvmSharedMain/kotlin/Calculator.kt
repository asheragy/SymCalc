import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


val viewModel = MainViewModel("2 + 1/2")

@Composable
fun Calculator() {
    val input = viewModel.display
    val mathText = viewModel.mathText
    val preview = viewModel.preview

    Column(Modifier.background(Color.DarkGray, RectangleShape)) {
        Text(text = input.value,
            Modifier.fillMaxWidth(),
            fontSize = 40.sp,
            textAlign = TextAlign.Right, color = Color.White)
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
