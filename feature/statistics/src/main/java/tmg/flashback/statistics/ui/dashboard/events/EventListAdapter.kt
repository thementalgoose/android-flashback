package tmg.flashback.statistics.ui.dashboard.events

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.Event
import tmg.flashback.statistics.databinding.ViewEventListItemBinding

class EventListAdapter: RecyclerView.Adapter<EventListViewHolder>() {

    var list: List<Event> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventListViewHolder(
            ViewEventListItemBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}