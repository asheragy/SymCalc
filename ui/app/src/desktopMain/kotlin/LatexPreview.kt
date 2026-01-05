import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import org.cerion.symcalc.expression.Expr
import org.scilab.forge.jlatexmath.TeXConstants
import org.scilab.forge.jlatexmath.TeXFormula
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane


@Composable
fun MathInputWithPreviewDesktop() {
    var input by remember {
        mutableStateOf("5 + 1/2")
    }

    val latex = remember(input) { Expr.parse(input).toLatex() }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Input (plain text)", style = MaterialTheme.typography.h6)

        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Try: 5 + 1/2") }
        )

        Text("Preview (read-only LaTeX)", style = MaterialTheme.typography.h6)

        // Scrollable preview area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp)
        ) {
            LatexPreviewDesktop(
                latex = latex,
                fontSizePx = 22f
            )
        }

        Divider()

        Text("Generated LaTeX:", style = MaterialTheme.typography.subtitle1)
        Text(
            latex,
            style = MaterialTheme.typography.body2,
            color = Color(0xFF555555.toInt()).toComposeColor()
        )
    }
}


@Composable
fun LatexPreviewDesktop(
    latex: String,
    fontSizePx: Float,
    modifier: Modifier = Modifier
) {
    SwingPanel(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 240.dp), // IMPORTANT on 1.7.1
        factory = {
            val label = JLabel("loading…").apply {
                foreground = Color.BLACK
                background = Color.YELLOW
                isOpaque = true
            }

            val scroll = JScrollPane(label).apply {
                border = null
                background = Color.CYAN
                isOpaque = true
                viewport.background = Color.CYAN
                viewport.isOpaque = true
            }

            JPanel(BorderLayout()).apply {
                background = Color.MAGENTA
                isOpaque = true
                add(scroll, BorderLayout.CENTER)
                putClientProperty("latexLabel", label)
            }
        },
        update = { panel ->
            val label = panel.getClientProperty("latexLabel") as JLabel
            try {
                val icon = TeXFormula(latex).createTeXIcon(TeXConstants.STYLE_DISPLAY, fontSizePx)
                label.icon = icon
                label.text = null
                label.preferredSize = Dimension(icon.iconWidth, icon.iconHeight)
            } catch (e: Exception) {
                label.icon = null
                label.text = "Parse error:\n$latex"
            }
            label.revalidate(); label.repaint()
            panel.revalidate(); panel.repaint()
        }
    )
}

/* -------------------- Small helper -------------------- */

private fun Color.toComposeColor(): androidx.compose.ui.graphics.Color =
    androidx.compose.ui.graphics.Color(this.red, this.green, this.blue, this.alpha)
