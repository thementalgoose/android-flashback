package tmg.flashback.home.trackpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_track_picker_year.view.*
import tmg.flashback.R
import tmg.flashback.utils.Selected

class TrackPickerYearAdapter: RecyclerView.Adapter<TrackPickerYearAdapter.ViewHolder>() {

    private var listeners: MutableList<(year: Int) -> Unit> = mutableListOf()

    var list: List<Selected<String>> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(TrackPickerYearCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    fun addListener(yearClicked: (year: Int) -> Unit) {
        listeners.add(yearClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_track_picker_year, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item.value, item.isSelected)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        private var year: Int = -1

        init {
            itemView.tvYear.setOnClickListener(this)
        }

        fun bind(label: String, isSelected: Boolean) {
            this.year = label.toInt()
            itemView.tvYear.text = label
            itemView.tvYear.isSelected = isSelected
        }

        override fun onClick(p0: View?) {
            listeners.forEach { it(year) }
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