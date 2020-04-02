package tmg.f1stats.season.race

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.f1stats.season.race.viewholders.RacePodiumViewHolder
import tmg.f1stats.season.race.viewholders.RaceResultViewHolder
import tmg.utilities.extensions.toEnum
import kotlin.math.ceil

class SeasonRaceAdapter(
    private val callback: SeasonRaceAdapterCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _viewType: SeasonRaceAdapterType = SeasonRaceAdapterType.RACE
    val viewType: SeasonRaceAdapterType
        get() = _viewType
    private var _list: List<SeasonRaceModel> = emptyList()
    val list: List<SeasonRaceModel>
        get() = _list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolderType: SeasonRaceAdapterViewHolderType? = viewType.toEnum<SeasonRaceAdapterViewHolderType>()
        val view = if (viewHolderType != null) {
            LayoutInflater.from(parent.context).inflate(viewHolderType.viewHolderRes, parent, false)
        } else {
            View(parent.context)
        }
        return when (viewHolderType) {
            SeasonRaceAdapterViewHolderType.RACE_PODIUM -> RacePodiumViewHolder(view)
            SeasonRaceAdapterViewHolderType.RACE_RESULT -> RaceResultViewHolder(view)
//            SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT -> SeasonRaceQualifyingResultViewHolder(callback, view)
            else -> throw Error("View type not implemented")
        }
    }

    override fun getItemCount(): Int = when (viewType) {
        SeasonRaceAdapterType.RACE -> list.size - 2
        SeasonRaceAdapterType.QUALIFYING_POS,
        SeasonRaceAdapterType.QUALIFYING_POS_1,
        SeasonRaceAdapterType.QUALIFYING_POS_2,
        SeasonRaceAdapterType.QUALIFYING_POS_3 -> list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            SeasonRaceAdapterType.RACE -> if (position == 0) SeasonRaceAdapterViewHolderType.RACE_PODIUM.ordinal else SeasonRaceAdapterViewHolderType.RACE_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS_1 -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS_2 -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS_3 -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
        }
    }

    fun update(type: SeasonRaceAdapterType, list: List<SeasonRaceModel>) {
        val beforeList = this.list
        val beforeState = this.viewType
        this._viewType = type
        this._list = list

        if (beforeList.isEmpty()) {
            for (x in list.indices) {
                notifyItemInserted(x)
            }
        } else if (beforeList.size == list.size && beforeState != type) {
            val result = DiffUtil.calculateDiff(DiffCalculator(
                    oldList = beforeList,
                    newList = list,
                    oldType = beforeState,
                    newType = type
            ))
            result.dispatchUpdatesTo(this)
        } else if (beforeList.size != list.size) {
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position).toEnum<SeasonRaceAdapterViewHolderType>()) {
            SeasonRaceAdapterViewHolderType.RACE_PODIUM -> {
                (holder as? RacePodiumViewHolder)?.bind(list[0], list[1], list[2])
            }
            SeasonRaceAdapterViewHolderType.RACE_RESULT -> {
                (holder as? RaceResultViewHolder)?.bind(list[position + 2])
            }
        }
    }

    //region Diff calculator

    inner class DiffCalculator(
            private val oldList: List<SeasonRaceModel>,
            private val newList: List<SeasonRaceModel>,
            private val oldType: SeasonRaceAdapterType,
            private val newType: SeasonRaceAdapterType
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].driver.id == newList[newItemPosition].driver.id

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = false
    }

    //endregion
}


enum class SeasonRaceAdapterType {
    RACE,
    QUALIFYING_POS_1,
    QUALIFYING_POS_2,
    QUALIFYING_POS_3,
    QUALIFYING_POS,
}

interface SeasonRaceAdapterCallback {
    fun orderBy(adapterType: SeasonRaceAdapterType)
}