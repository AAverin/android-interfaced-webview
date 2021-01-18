package apps.interfaced.webview.sample

import android.util.Log
import android.view.View
import android.widget.TextView
import apps.interfaced.webview.sample.cmpntr.Cmpnt
import apps.interfaced.webview.sample.models.TextData

class TextCmpnt : Cmpnt<TextData>(R.layout.cmpnt_text) {

    private lateinit var textView: TextView

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        textView = view.findViewById(R.id.text)
    }

    override fun setData(data: TextData) {
        super.setData(data)
        textView.text = data.text
    }

    override fun onVisible() {
        super.onVisible()
        Log.d("TextCmpnt", "onVisible")
    }

    override fun onHidden() {
        super.onHidden()
        Log.d("TextCmpnt", "onHidden")
    }
}