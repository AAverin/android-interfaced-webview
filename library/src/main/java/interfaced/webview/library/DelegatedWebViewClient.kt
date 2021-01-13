package interfaced.webview.library

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import java.util.concurrent.CopyOnWriteArrayList

interface WebViewClientInterface {
    fun onUrlClicked(url: String): Boolean {
        return false
    }
    fun onPageFinished(url: String) {}
    fun onPageStarted(url: String) {}
    fun onError(error: Error, failingUrl: String?) {}
}

open class DelegatedWebViewClient(
    private val config: FeaturesConfig
) : WebViewClient() {

    private val delegates = CopyOnWriteArrayList<WebViewClientInterface>()
    fun addDelegate(delegate: WebViewClientInterface) {
        delegates.add(delegate)
    }

    fun removeDelegate(delegate: WebViewClientInterface) {
        delegates.remove(delegate)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        request ?: return super.shouldInterceptRequest(view, request)

        if (config.fixFaviconLoadErrors) {
            if (!request.isForMainFrame && request.url.path.isFavIconUrl()) {
                try {
                    return WebResourceResponse("image/png", null, null)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        url ?: return super.shouldInterceptRequest(view, url)

        if (config.fixFaviconLoadErrors) {
            if (url.toLowerCase().contains("/favicon.ico")) {
                return WebResourceResponse("image/png", null, null)
            }
        }

        return null
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url ?: return super.shouldOverrideUrlLoading(view, url)

        return if (delegates.toList().any {
                it.onUrlClicked(url)
            }) {
            true
        } else super.shouldOverrideUrlLoading(view, url)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request ?: return super.shouldOverrideUrlLoading(view, request)

        request.url.toString().apply {
            return shouldOverrideUrlLoading(view, this)
        }

        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        url?.let {
            delegates.toList().forEach {
                it.onPageFinished(url)
            }
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        url?.let {
            delegates.toList().forEach {
                it.onPageStarted(url)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        if (request?.url?.path.isFavIconUrl()) {
            return
        }

        sendError(error?.let { WebViewResourceError(it, request) }, request?.url.toString())
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        if (config.fixFaviconLoadErrors) {
            if (failingUrl.isFavIconUrl()) {
                return
            }
        }

        sendError(WebViewResourceError(errorCode, description, failingUrl), failingUrl)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)

        if (config.fixFaviconLoadErrors) {
            if (request?.url?.path.isFavIconUrl()) {
                return
            }
        }


        sendError(errorResponse?.let { WebViewHttpError(it, request) }, request?.url.toString())
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)

        sendError(error?.let { WebViewSSLError(it) }, view?.url)
    }

    private fun sendError(error: Error?, failingUrl: String?) {
        delegates.toList().forEach {
            it.onError(error ?: WebViewError, failingUrl)
        }
    }

    private fun String?.isFavIconUrl() = this?.endsWith("favicon.ico") ?: false

}