package interfaced.webview.library

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import java.net.URLEncoder
import java.nio.charset.Charset

class InterfacedWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(
    context,
    attrs,
    defStyleAttr
) {

    fun setup(config: FeaturesConfig, nativeInterface: NativeInterface) {
        settings.javaScriptEnabled = true

        addJavascriptInterface(JSAsync(this, nativeInterface), "JSNativeAsync")
        webViewClient = DelegatedWebViewClient(config).apply {
            addDelegate(object : WebViewClientInterface {
                override fun onPageFinished(url: String) {
                    injectJs("native.js")

                    if (config.supportHeightUpdates) {
                        injectJs("height.js")
                    }
                }
            })
        }
    }

    private fun injectJs(assetName: String) {
        try {
            with(context.assets.open(assetName)) {
                val bytes = this.readBytes()
                val base64Encoded = Base64.encodeToString(bytes, Base64.NO_WRAP)
                loadUrl(
                    """
javascript:(function() {
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.innerHTML = window.atob('$base64Encoded');
    head.appendChild(script);
})();
            """.trimIndent()
                )

                this.close()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}