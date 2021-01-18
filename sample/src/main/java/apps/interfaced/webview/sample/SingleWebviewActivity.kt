package apps.interfaced.webview.sample

import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import interfaced.webview.library.FeaturesConfig
import interfaced.webview.library.NativeInterface
import kotlinx.android.synthetic.main.single_webview_activity.*
import kotlinx.serialization.json.JsonElement

class SingleWebviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_webview_activity)

        webview.setup(
            FeaturesConfig(),
            object : NativeInterface {

            }, {
                Log.d("SampleActivity", "onBodyHeightChanged $it")
            }
        )
        webview.makeDebuggable()

        button.setOnClickListener {
            val urlText = url.text.toString()

            if (urlText.isEmpty() || !URLUtil.isValidUrl(urlText)) {
                Toast.makeText(this, "Please provide some valid url", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            webview.loadUrl(urlText)
        }
    }

}