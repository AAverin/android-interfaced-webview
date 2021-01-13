package interfaced.webview.library

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class JSAsync(
    private val webview: InterfacedWebView,
    private val nativeInterface: NativeInterface,
    private val onUpdateHeight: ((Int) -> Unit)? = null
) {

    @JavascriptInterface
    fun run(handler: String, method: String, parametersJson: String) {
        webview.post {
            try {
                val json: JsonElement = Json.parseToJsonElement(parametersJson)
                Log.d("JSAsync", "running $handler method $method with $json")
                val result =
                    nativeInterface.javaClass.getMethod(method, JsonElement::class.java)
                        .invoke(nativeInterface, json)
                if (result != null && result !is String) {
                    throw IllegalArgumentException("Result of the method call is not a String. Only strings/valid json strings are supported")
                }
                resolve(handler, result as String?)
            } catch (e: NoSuchMethodException) {
                Log.e("JSAsync", "No such method!", e)
                e.printStackTrace()
                reject(handler, e.message.toString())
            } catch (e: Exception) {
                Log.e("JSAsync", "error")
                e.printStackTrace()
                reject(handler, e.message.toString())
            }
        }
    }

    @JavascriptInterface
    fun updateHeight(height: String) {
        onUpdateHeight?.invoke(height.toInt())
    }

    private fun reject(handler: String, error: String) {
        webview.post {
            webview.loadUrl("javascript:$handler.rejectNative('$error')")
        }
    }

    private fun resolve(handler: String, result: String? = "") {
        webview.post {
            if (result != null) {
                webview.loadUrl("javascript:$handler.resolveNative($result)")
            } else {
                webview.loadUrl("javascript:$handler.resolveNative()")
            }
        }
    }
}