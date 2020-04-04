package tmg.f1stats.home.trackpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_track_picker_year.view.*
import tmg.f1stats.R
import tmg.f1stats.utils.Selected

class TrackPickerYearAdapter: RecyclerView.Adapter<TrackPickerYearAdapter.ViewHolder>() {

    var list: List<Selected<String>> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(TrackPickerYearCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_track_picker_year, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item.value, item.isSelected)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(label: String, isSelected: Boolean) {
            itemView.tvYear.text = label
            itemView.tvYear.isSelected = isSelected
        }
    }

    inner class TrackPickerYearCallback(
        private val oldList: List<Selected<String>>,
        private val newList: List<Selected<String>>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsTheSame(oldItemPosition, newItemPosition)

    }
}