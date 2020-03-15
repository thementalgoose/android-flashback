package tmg.f1stats.season.race

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
            SeasonRaceAdapterViewHolderType.RACE_PODIUM -> SeasonRaceRacePodiumViewHolder(view)
            SeasonRaceAdapterViewHolderType.RACE_RESULT -> SeasonRaceRaceResultViewHolder(view)
            SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT -> SeasonRaceQualifyingResultViewHolder(callback, view)
            SeasonRaceAdapterViewHolderType.QUALIFYING_GRID -> SeasonRaceQualifyingGridViewHolder(view)
            null -> throw Error("View type not implemented")
        }
    }

    override fun getItemCount(): Int = when (viewType) {
        SeasonRaceAdapterType.RACE -> list.size - 2
        SeasonRaceAdapterType.QUALIFYING_POS -> list.size
        SeasonRaceAdapterType.QUALIFYING_POS_1 -> list.size
        SeasonRaceAdapterType.QUALIFYING_POS_2 -> list.size
        SeasonRaceAdapterType.QUALIFYING_POS_3 -> list.size
        SeasonRaceAdapterType.QUALIFYING_GRID -> ceil(list.size / 2f).toInt()
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            SeasonRaceAdapterType.RACE -> if (position == 0) SeasonRaceAdapterViewHolderType.RACE_PODIUM.ordinal else SeasonRaceAdapterViewHolderType.RACE_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS_1 -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS_2 -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS_3 -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_GRID -> SeasonRaceAdapterViewHolderType.QUALIFYING_GRID.ordinal
        }
    }

    fun update(type: SeasonRaceAdapterType, list: List<SeasonRaceModel>) {
        val beforeList = this.list
        val beforeState = this.viewType
        println("UPDATE $beforeState $type - ${beforeList.size} ${list.size}")
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
                (holder as? SeasonRaceRacePodiumViewHolder)?.bind(list[0], list[1], list[2])
            }
            SeasonRaceAdapterViewHolderType.RACE_RESULT -> {
                (holder as? SeasonRaceRaceResultViewHolder)?.bind(list[position + 2])
            }
            SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT -> {
                (holder as? SeasonRaceQualifyingResultViewHolder)?.bind(list[position])
            }
            SeasonRaceAdapterViewHolderType.QUALIFYING_GRID -> {
                val driver1: SeasonRaceModel = list[position * 2]
                val driver2: SeasonRaceModel? = list.getOrNull((position * 2) + 1)
                (holder as? SeasonRaceQualifyingGridViewHolder)?.bind(driver1, driver2)
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

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].driver.driverId == newList[newItemPosition].driver.driverId

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
    QUALIFYING_GRID
}

interface SeasonRaceAdapterCallback {
    fun orderBy(adapterType: SeasonRaceAdapterType)
}