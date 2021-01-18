package apps.interfaced.webview.sample.cmpntr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class CmpntViewHolder<T>(val cmpnt: Cmpnt<T>, view: View) : RecyclerView.ViewHolder(view)

abstract class Cmpnt<T>(@field:LayoutRes private val cmpntLayoutId: Int) {

    open var isRecycleable: Boolean = true

    protected lateinit var context: Context

    open fun setData(data: T) {}

    protected open fun onViewCreated(view: View) {}

    fun createView(inflater: LayoutInflater, parent: ViewGroup?): View {
        val view = inflater.inflate(cmpntLayoutId, parent, false)
        onViewCreated(view)
        this.context = view.context
        return view
    }

    open fun onVisible() {}

    open fun onHidden() {}
}