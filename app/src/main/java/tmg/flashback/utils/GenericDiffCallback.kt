package tmg.flashback.utils

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

inline fun <reified T> RecyclerView.Adapter<*>.calculateDiff(oldList: List<T>, newList: List<T>): DiffUtil.DiffResult {
    return DiffUtil.calculateDiff(GenericDiffCallback(oldList, newList))
}

class GenericDiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(o: Int, n: Int) = oldList[o] == newList[n]

    override fun areContentsTheSame(o: Int, n: Int) = oldList[o] == newList[n]

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size
}