
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import graph.MyChart

@Composable
@Preview
fun App() {
    MaterialTheme {
        //Calculator()
        MyChart()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

val viewModel = MainViewModel()
@Composable
fun Calculator() {
    val display = viewModel.display
    val preview = viewModel.preview
    //val display: String by viewModel.display.observeAsState("")
    //val preview: String by viewModel.preview.observeAsState(viewModel.preview.value!!)

    Column(Modifier.background(Color.DarkGray, RectangleShape)) {
        Text(text = display.value,
            Modifier.fillMaxWidth(),
            fontSize = 40.sp,
            textAlign = TextAlign.Right, color = Color.White)
        Text(text = preview.value,
            Modifier.fillMaxWidth(),
            fontSize = 30.sp,
            textAlign = TextAlign.Right, color = Color.LightGray)
        KeyPad {
            viewModel.onKey(it)
        }
    }

}
