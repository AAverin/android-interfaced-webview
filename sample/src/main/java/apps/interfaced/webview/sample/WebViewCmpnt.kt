package apps.interfaced.webview.sample

import android.util.Log
import android.view.View
import apps.interfaced.webview.sample.cmpntr.Cmpnt
import apps.interfaced.webview.sample.models.WebViewData
import interfaced.webview.library.FeaturesConfig
import interfaced.webview.library.InterfacedWebView
import interfaced.webview.library.NativeInterface
import kotlinx.android.synthetic.main.cmpnt_webview.*
import java.lang.annotation.Native

class WebViewCmpnt : Cmpnt<WebViewData>(R.layout.cmpnt_webview) {

    private lateinit var webview: InterfacedWebView

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        webview = view.findViewById(R.id.interfaced_webview)

        //Do this for debuggable builds only!
        webview.makeDebuggable()
    }

    override fun onVisible() {
        super.onVisible()
        Log.d("WebViewCmpnt", "onVisible")
        webview.setup(
            FeaturesConfig(),
            object : NativeInterface {},
            onUpdateHeight = {
                webview.animateToHeight(it, 300)
            }
        )
    }

    override fun onHidden() {
        super.onHidden()
        Log.d("WebViewCmpnt", "onHidden")
        webview.clean()
    }

    override fun setData(data: WebViewData) {
        super.setData(data)

        data.url?.let {
            webview.loadUrl(it)
        }
    }
}