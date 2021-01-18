package apps.interfaced.webview.sample

import android.util.Log
import apps.interfaced.webview.sample.cmpntr.Cmpnt
import apps.interfaced.webview.sample.models.SeparatorData

class SeparatorCmpnt : Cmpnt<SeparatorData>(R.layout.cmpnt_separator) {
    override fun onVisible() {
        super.onVisible()
        Log.d("SeparatorCmpnt", "onVisible")
    }

    override fun onHidden() {
        super.onHidden()
        Log.d("SeparatorCmpnt", "onHidden")
    }
}