package apps.nmt.webview.sample

import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.sample_activity.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SampleActivity : AppCompatActivity() {

    private val payload = mapOf(
        "demokey1" to "demovalue1",
        "demokey2" to "demovalue2"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)

        WebView.setWebContentsDebuggingEnabled(true)

        button.setOnClickListener {
            val urlText = url.text.toString()

            if (urlText.isEmpty() || !URLUtil.isValidUrl(urlText)) {
                Toast.makeText(this, "Please provide some valid url", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            webview.postUrl(urlText, Json.encodeToString(payload).toByteArray())
            Toast.makeText(this, "Posted payload $payload", Toast.LENGTH_SHORT).show()
        }
    }

}