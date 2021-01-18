package apps.interfaced.webview.sample.cmpntr

import androidx.recyclerview.widget.DiffUtil

class SimpleDiffUtilCallback<T>(
    protected val newItems: List<T>,
    protected val oldItems: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int) = newItems[newPosition] == oldItems[oldPosition]
    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = newItems[newPosition] == oldItems[oldPosition]
}