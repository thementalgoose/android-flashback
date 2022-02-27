package tmg.flashback.statistics.ui.shared.tyres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewTyreHeaderBinding
import tmg.flashback.statistics.databinding.ViewTyreItemBinding
import tmg.flashback.statistics.ui.shared.tyres.viewholder.HeaderViewHolder
import tmg.flashback.statistics.ui.shared.tyres.viewholder.ItemViewHolder
import tmg.utilities.difflist.GenericDiffCallback

class TyresAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<TyreItem> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_tyre_header -> HeaderViewHolder(
                ViewTyreHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_tyre_item -> ItemViewHolder(
                ViewTyreItemBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw RuntimeException("Tyre type not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is TyreItem.Header -> (holder as HeaderViewHolder).bind(item)
            is TyreItem.Item -> (holder as ItemViewHolder).bind(item)
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = list[position].layoutId
}