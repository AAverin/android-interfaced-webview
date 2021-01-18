package apps.interfaced.webview.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import apps.interfaced.webview.sample.cmpntr.Cmpnt
import apps.interfaced.webview.sample.cmpntr.CmpntrRecyclerAdapter
import apps.interfaced.webview.sample.models.SeparatorData
import apps.interfaced.webview.sample.models.TextData
import apps.interfaced.webview.sample.models.WebViewData
import kotlinx.android.synthetic.main.recycler_webviews_activity.*
import kotlinx.android.synthetic.main.single_webview_activity.*

class RecyclerWebViewsActivity : AppCompatActivity() {

    private lateinit var adapter: CmpntrRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_webviews_activity)

        adapter = CmpntrRecyclerAdapter(layoutInflater)
        adapter.register { WebViewCmpnt() }
        adapter.register { TextCmpnt() }
        adapter.register { SeparatorCmpnt() }

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        adapter.setItems(
            listOf(
                TextData("1", "GiphyTest"),
                SeparatorData("2"),
                WebViewData(
                    "3",
                    url = "https://media.giphy.com/media/84SFZf1BKgzeny1WxQ/giphy.gif"
                ),
                SeparatorData("4"),
                TextData("5", "LongWebviewTest"),
                SeparatorData("6"),
                WebViewData("7", url = "https://www.welt.de/finanzen/verbraucher/article224532588/Anmeldung-fehlgeschlagen-das-grosse-Versagen-bei-den-Impfterminen.html"),
                SeparatorData("8"),
                WebViewData("9", url = "https://www.welt.de/politik/deutschland/plus224488760/Opfer-der-Corona-Politik-Bei-mir-waechst-die-Wut-auf-die-Regierung.html")
            )
        )

    }
}