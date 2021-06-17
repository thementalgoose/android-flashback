package tmg.flashback.upnext.ui.timezone

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.upnext.R
import tmg.flashback.upnext.databinding.ViewTimezoneBinding

class TimezoneAdapter: RecyclerView.Adapter<TimezoneViewHolder>() {

    var list: List<TimezoneItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimezoneViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TimezoneViewHolder(ViewTimezoneBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TimezoneViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}