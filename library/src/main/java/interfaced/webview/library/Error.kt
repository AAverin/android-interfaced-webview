package interfaced.webview.library

import android.net.http.SslError
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse

open class Error(message: String, cause: Throwable? = null) : Throwable(message, cause)
class WebViewResourceError(message: String) : Error(message) {
    constructor(errorCode: Int?, description: String?, failingUrl: String?) : this(
        "Error in WebView, failed to request $failingUrl, $errorCode|$description"
    )

    constructor(error: WebResourceError? = null, request: WebResourceRequest? = null) : this(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            "HttpError in WebView, " +
                    "${request?.let { "failed requesting ${it.url} " }}, " +
                    "${error?.let { "response ${it.errorCode}|${it.description}" }}"
        else "HttpError in WebView"
    )
}
class WebViewHttpError(errorResponse: WebResourceResponse? = null,
                       request: WebResourceRequest? = null) : Error(
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        "HttpError in WebView, " +
                "${request?.let { "failed requesting ${it.url} " }}, " +
                "${errorResponse?.let { "response ${it.reasonPhrase}" }}"
    else "HttpError in WebView"
)
class WebViewSSLError(sslError: SslError) : Error("SSLError in WebView $sslError")
val WebViewError = Error("Error in WebView")
