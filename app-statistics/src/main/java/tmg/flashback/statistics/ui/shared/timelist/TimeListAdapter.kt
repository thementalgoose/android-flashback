package tmg.flashback.statistics.ui.shared.timelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.core.model.UpNextScheduleTimestamp
import tmg.flashback.statistics.databinding.ViewSeasonListUpNextTimelistBinding
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.statistics.ui.shared.timelist.viewholders.TimeViewHolder

class TimeListAdapter: RecyclerView.Adapter<TimeViewHolder>() {

    var list: List<TimeListItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layout = ViewSeasonListUpNextTimelistBinding.inflate(layoutInflater, parent, false)
        return TimeViewHolder(layout)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size


}