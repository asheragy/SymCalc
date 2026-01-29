
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import org.scilab.forge.jlatexmath.TeXConstants
import org.scilab.forge.jlatexmath.TeXFormula
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingConstants


@Composable
actual fun LatexView(
    latex: String,
    modifier: Modifier
) {
    SwingPanel(
        modifier = modifier.heightIn(min = 60.dp),
        factory = {
            val label = JLabel("loading…").apply {
                foreground = java.awt.Color.WHITE
                background = java.awt.Color.DARK_GRAY
                isOpaque = true
                horizontalAlignment = SwingConstants.RIGHT
            }

            val scroll = JScrollPane(label).apply {
                border = null
                background = java.awt.Color.CYAN
                isOpaque = true
                viewport.background = java.awt.Color.CYAN
                viewport.isOpaque = true
            }

            JPanel(BorderLayout()).apply {
                background = java.awt.Color.MAGENTA
                isOpaque = true
                add(scroll, BorderLayout.CENTER)
                putClientProperty("latexLabel", label)
            }
        },
        update = { panel ->
            val label = panel.getClientProperty("latexLabel") as JLabel
            try {
                val icon = TeXFormula(latex).createTeXIcon(TeXConstants.STYLE_DISPLAY, 18f)
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
