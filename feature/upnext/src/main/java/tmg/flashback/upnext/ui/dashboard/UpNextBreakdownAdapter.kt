package tmg.flashback.upnext.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.upnext.R
import tmg.flashback.upnext.databinding.ViewBreakdownDayBinding
import tmg.flashback.upnext.databinding.ViewBreakdownDividerBinding
import tmg.flashback.upnext.databinding.ViewBreakdownItemBinding
import tmg.flashback.upnext.ui.dashboard.viewholder.DayViewHolder
import tmg.flashback.upnext.ui.dashboard.viewholder.DividerViewHolder
import tmg.flashback.upnext.ui.dashboard.viewholder.ItemViewHolder
import tmg.utilities.difflist.GenericDiffCallback
import java.lang.RuntimeException

class UpNextBreakdownAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<UpNextBreakdownModel> = emptyList()
        set(value) {
            println(value)
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshUpNext() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_breakdown_day -> DayViewHolder(
                ViewBreakdownDayBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_breakdown_item -> ItemViewHolder(
                ViewBreakdownItemBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_breakdown_divider -> DividerViewHolder(
                ViewBreakdownDividerBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw RuntimeException("View type not supported!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is UpNextBreakdownModel.Day -> (holder as DayViewHolder).bind(item)
            is UpNextBreakdownModel.Item -> (holder as ItemViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int = list[position].layoutId

    override fun getItemCount(): Int = list.size
}