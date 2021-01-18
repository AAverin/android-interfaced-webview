package apps.interfaced.webview.sample.cmpntr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CmpntrRecyclerAdapter(private val layoutInflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(false)
    }

    private var items: List<Any> = listOf()
    fun setItems(newItems: List<Any>): Boolean {
        val oldItems = items
        items = newItems as MutableList<Any>

        DiffUtil.calculateDiff(SimpleDiffUtilCallback(newItems, oldItems)).dispatchUpdatesTo(this)

        return oldItems != newItems
    }

    //public to be accessible by `register` inline function
    var cmpntProviders = LinkedHashMap<Class<out Any>, () -> Cmpnt<*>>()

    inline fun <reified T : Any> register(noinline cmpntProvider: () -> Cmpnt<out T>): Int {
        cmpntProviders[T::class.java] = cmpntProvider
        return cmpntProviders.size
    }

    override fun getItemViewType(position: Int) = cmpntProviders.keys.indexOf(items[position].javaClass)
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cmpnt = cmpntProviders.values.elementAt(viewType)()
        return CmpntViewHolder(cmpnt, cmpnt.createView(layoutInflater, parent))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CmpntViewHolder<Any>).apply {
            cmpnt.setData(items[position])
            if (!cmpnt.isRecycleable)
                setIsRecyclable(false)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as CmpntViewHolder<Any>).cmpnt.onVisible()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as CmpntViewHolder<Any>).cmpnt.onHidden()
    }
}