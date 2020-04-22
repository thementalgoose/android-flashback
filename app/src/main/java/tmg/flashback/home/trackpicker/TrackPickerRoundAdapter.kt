package tmg.flashback.home.trackpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_track_picker_track.view.*
import tmg.flashback.R
import tmg.flashback.utils.Selected
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.flashback.utils.localLog

class TrackPickerRoundAdapter: RecyclerView.Adapter<TrackPickerRoundAdapter.ViewHolder>() {

    private var listeners: MutableList<(year: TrackModel) -> Unit> = mutableListOf()

    var list: List<Selected<TrackModel>> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(TrackPickerRoundCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    fun addListener(yearClicked: (year: TrackModel) -> Unit) {
        listeners.add(yearClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_track_picker_track, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item.value, item.isSelected)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var trackModel: TrackModel

        init {
            itemView.clItem.setOnClickListener(this)
        }

        fun bind(model: TrackModel, isSelected: Boolean) {
            this.trackModel = model
            itemView.imgCountry.setImageResource(itemView.context.getFlagResourceAlpha3(model.countryKey))
            itemView.clItem.isSelected = isSelected
            itemView.tvCountry.text = model.country
        }

        override fun onClick(p0: View?) {
            listeners.forEach { it(trackModel) }
        }
    }

    inner class TrackPickerRoundCallback(
        private val oldList: List<Selected<TrackModel>>,
        private val newList: List<Selected<TrackModel>>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].value.circuitName == newList[newItemPosition].value.circuitName &&
                    oldList[oldItemPosition].value.country == newList[newItemPosition].value.country &&
                    oldList[oldItemPosition].value.countryKey == newList[newItemPosition].value.countryKey &&
                    oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsTheSame(oldItemPosition, newItemPosition) &&
                oldList[oldItemPosition].value.season == newList[newItemPosition].value.season &&
                oldList[oldItemPosition].value.round == newList[newItemPosition].value.round

    }
}