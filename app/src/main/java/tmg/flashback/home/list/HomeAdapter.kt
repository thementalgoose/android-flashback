package tmg.flashback.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.home.list.viewholders.ConstructorViewHolder
import tmg.flashback.home.list.viewholders.DriverViewHolder
import tmg.flashback.home.list.viewholders.TrackViewHolder

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<HomeItem> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_home_track -> TrackViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_home_driver -> DriverViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_home_constructor -> ConstructorViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> throw Exception("Type in HomeItem is not implemented on onCreateViewHolder")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is HomeItem.Track -> (holder as TrackViewHolder).bind(item)
            is HomeItem.Driver -> (holder as DriverViewHolder).bind(item)
            is HomeItem.Constructor -> (holder as ConstructorViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = list[position].layoutId

    override fun getItemCount(): Int = list.size

    inner class DiffCallback(
        private val oldList: List<HomeItem>,
        private val newList: List<HomeItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(o: Int, n: Int) = oldList[o] == newList[n] ||
                areTracksTheSame(o, n)

        override fun areContentsTheSame(o: Int, n: Int) = oldList[o] == newList[n]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        private fun areTracksTheSame(old: Int, new: Int): Boolean {
            val oldItem = oldList[old] as? HomeItem.Track
            val newItem = newList[old] as? HomeItem.Track
            if (oldItem != null && newItem != null) {
                return oldItem.raceName == newItem.raceName &&
                        oldItem.raceCountryISO == newItem.raceCountryISO
            }
            return false
        }
    }
}