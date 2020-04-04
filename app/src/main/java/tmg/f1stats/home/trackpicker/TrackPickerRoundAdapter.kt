package tmg.f1stats.home.trackpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_track_picker_track.view.*
import tmg.f1stats.R
import tmg.f1stats.utils.Selected
import tmg.f1stats.utils.getFlagResourceAlpha3

class TrackPickerRoundAdapter: RecyclerView.Adapter<TrackPickerRoundAdapter.ViewHolder>() {

    var list: List<Selected<TrackModel>> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(TrackPickerRoundCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_track_picker_track, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item.value, item.isSelected)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(model: TrackModel, isSelected: Boolean) {
            itemView.imgCountry.setImageResource(itemView.context.getFlagResourceAlpha3(model.countryKey))
            itemView.clItem.isSelected = isSelected
            itemView.tvCountry.text = model.country
        }
    }

    inner class TrackPickerRoundCallback(
        private val oldList: List<Selected<TrackModel>>,
        private val newList: List<Selected<TrackModel>>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsTheSame(oldItemPosition, newItemPosition)

    }
}