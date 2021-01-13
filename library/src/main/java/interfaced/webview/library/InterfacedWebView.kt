package interfaced.webview.library

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Base64
import android.view.animation.AccelerateInterpolator
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import interfaced.webview.library.animation.SlideAnimation
import kotlinx.serialization.descriptors.PrimitiveKind
import java.net.URLEncoder
import java.nio.charset.Charset
import kotlin.math.roundToInt

class InterfacedWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(
    context,
    attrs,
    defStyleAttr
) {

    fun setup(
        config: FeaturesConfig,
        nativeInterface: NativeInterface,
        onUpdateHeight: ((Int) -> Unit)? = null
    ) {
        settings.javaScriptEnabled = true

        addJavascriptInterface(JSAsync(this, nativeInterface, onUpdateHeight), "JSNativeAsync")
        webViewClient = DelegatedWebViewClient(config).apply {
            addDelegate(object : WebViewClientInterface {
                override fun onPageFinished(url: String) {
                    injectScripts(config.supportHeightUpdates)
                }
            })
        }
    }

    fun injectScripts(supportHeightUpdates: Boolean) {
        injectJs("native.js")

        if (supportHeightUpdates) {
            injectJs("height.js")
        }
    }

//    /**
//     * Scales proportionally to cover content
//     * Can be used to scale Gif images
//     */
//    fun scaleTo(contentWidth: Int, contentHeight: Int) {
//        reset()
//        val scale = contentWidth / width.toDouble()
//        val webViewHeight = contentHeight / scale
//        setHeight(webViewHeight.roundToInt())
//        settings.setLoadWithOverviewMode(true)
//        settings.setUseWideViewPort(true)
//    }

    fun setHeight(height: Int) {
        reset()
        layoutParams.height = height
        requestLayout()
    }

    fun animateToHeight(height: Int, duration: Long) {
        reset()
        val anim = SlideAnimation(this, height)
        anim.interpolator = AccelerateInterpolator()
        anim.duration = duration
        clearAnimation()
        startAnimation(anim)
    }

    fun makeDebuggable() {
        setWebContentsDebuggingEnabled(true)
    }

    private fun reset() {
        settings.setLoadWithOverviewMode(false)
        settings.setUseWideViewPort(false)
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