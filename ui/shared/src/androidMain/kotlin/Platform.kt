
import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject

private const val KATEX_HTML = """
<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.16.25/dist/katex.min.css" />
  <style>
    html, body {
      margin: 0;
      padding: 0;
      background: transparent;
    }
    #math {
      color: #FFFFFF;
      font-size: 1.15rem;
      min-height: 60px;
      text-align: right;
      overflow-x: auto;
      white-space: normal;
      padding: 6px 0;
    }
    #math .katex-display {
      margin: 0 !important;
      text-align: right !important;
    }
    #math .katex-display > .katex {
      text-align: right !important;
    }
  </style>
  <script>
    let pendingLatex = "";

    function renderLatex(tex) {
      pendingLatex = tex ?? "";
      const container = document.getElementById("math");
      if (!container) return;

      if (!window.katex) {
        container.textContent = pendingLatex;
        return;
      }

      try {
        window.katex.render(pendingLatex, container, {
          displayMode: true,
          throwOnError: false,
          strict: "ignore"
        });
      } catch (_) {
        container.textContent = pendingLatex;
      }
    }
  </script>
  <script defer src="https://cdn.jsdelivr.net/npm/katex@0.16.25/dist/katex.min.js" onload="renderLatex(pendingLatex)"></script>
</head>
<body>
  <div id="math"></div>
</body>
</html>
"""

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun LatexView(latex: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String?) {
                        val currentLatex = view.tag as? String ?: return
                        view.evaluateJavascript("window.renderLatex(${JSONObject.quote(currentLatex)});", null)
                    }
                }
                loadDataWithBaseURL(
                    "https://cdn.jsdelivr.net/",
                    KATEX_HTML,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        update = { webView ->
            webView.tag = latex
            webView.evaluateJavascript("window.renderLatex(${JSONObject.quote(latex)});", null)
        }
    )
}
