package apps.interfaced.webview.sample

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.sample_activity.*

class SampleActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)

        button_single_webview.setOnClickListener {
            startActivity(Intent(applicationContext, SingleWebviewActivity::class.java))
        }

        button_recycler_webviews.setOnClickListener {
            startActivity(Intent(applicationContext, RecyclerWebViewsActivity::class.java))
        }
    }
}