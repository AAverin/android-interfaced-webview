package interfaced.webview.library

import android.webkit.JavascriptInterface
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * Implement this interface and add methods that you will support being called from Javascript
 *
 * Every method can have one optional JsonElement parameter
 * Every method has to return a String that is a valid Json String
 */
interface NativeInterface {
}

