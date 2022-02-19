package tmg.flashback.regulations.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.regulations.R
import tmg.flashback.regulations.databinding.*
import tmg.flashback.regulations.domain.Item
import tmg.flashback.regulations.ui.items.viewholders.DeterminesViewHolder
import tmg.flashback.regulations.ui.items.viewholders.*
import java.lang.RuntimeException

internal class ItemsAdapter(
    private val setSection: (label: Int, isExpanded: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<Item> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_format_header -> HeaderViewHolder(
                ViewFormatHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_format_subheader -> SubHeaderViewHolder(
                ViewFormatSubheaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_format_text -> TextViewHolder(
                ViewFormatTextBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_format_tyres -> TyresViewHolder(
                ViewFormatTyresBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_format_stat -> StatViewHolder(
                ViewFormatStatBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_format_progress_bar -> ProgressBarViewHolder(
                ViewFormatProgressBarBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_format_spacer -> SpacerViewHolder(
                ViewFormatSpacerBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_format_led_to -> DeterminesViewHolder(
                ViewFormatLedToBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_format_collapsible -> CollapsibleViewHolder(
                ViewFormatCollapsibleBinding.inflate(layoutInflater, parent, false),
                setSection
            )
            else -> throw RuntimeException("View holder for layout file not specified")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is Item.Header -> (holder as HeaderViewHolder).bind(item)
            is Item.SubHeader -> (holder as SubHeaderViewHolder).bind(item)
            is Item.ProgressBar -> (holder as ProgressBarViewHolder).bind(item)
            is Item.Text -> (holder as TextViewHolder).bind(item)
            is Item.Tyres -> (holder as TyresViewHolder).bind(item)
            is Item.Stat -> (holder as StatViewHolder).bind(item)
            is Item.Determines -> (holder as DeterminesViewHolder).bind(item)
            is Item.Collapsible -> (holder as CollapsibleViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = list[position].layoutId

    override fun getItemCount() = list.size

    inner class DiffCallback(
        private val oldList: List<Item>,
        private val newList: List<Item>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem || isSameCollapsibleView(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        private fun isSameCollapsibleView(oldItem: Item, newItem: Item): Boolean {
            return oldItem is Item.Collapsible && newItem is Item.Collapsible && oldItem.label == newItem.label
        }
    }
}