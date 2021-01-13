package apps.interfaced.webview.sample

import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import interfaced.webview.library.FeaturesConfig
import interfaced.webview.library.NativeInterface
import kotlinx.android.synthetic.main.sample_activity.*
import kotlinx.serialization.json.JsonElement

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)

        webview.setup(
            FeaturesConfig(),
            object : NativeInterface {
                override fun onBodyHeightChanged(height: JsonElement?) {
                    Log.d("SampleActivity", "onBodyHeightChanged $height")
                }

            }
        )
        WebView.setWebContentsDebuggingEnabled(true)

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