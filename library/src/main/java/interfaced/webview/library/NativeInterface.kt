package interfaced.webview.library

import android.webkit.JavascriptInterface
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

interface NativeInterface {
    fun onBodyHeightChanged(height: JsonElement?)
}

