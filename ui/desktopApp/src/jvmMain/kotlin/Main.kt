
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    MaterialTheme {
        Calculator()
        //Graph()
        //MathEditorDemoScreenWithKeypad()


        //LatexPreviewDesktop(latex = "2 + \\frac{1}{2}")
        //SwingPanelSmokeTest()


        //ComposeMultiplatformBasicLineChart()
        //SinX()
    }
}

@Composable
fun MathText(text: String) {
    Text(text = text,
        Modifier.fillMaxWidth(),
        fontSize = 40.sp,
        textAlign = TextAlign.Right, color = Color.White)
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
