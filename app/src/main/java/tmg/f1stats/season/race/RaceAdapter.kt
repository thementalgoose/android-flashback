package tmg.f1stats.season.race

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.f1stats.R
import tmg.f1stats.season.race.viewholders.RacePodiumViewHolder
import tmg.f1stats.season.race.viewholders.RaceResultHeaderViewHolder
import tmg.f1stats.season.race.viewholders.RaceResultViewHolder
import tmg.utilities.extensions.toEnum

class RaceAdapter(
    private val callback: RaceAdapterCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _viewType: RaceAdapterType = RaceAdapterType.RACE
    val viewType: RaceAdapterType
        get() = _viewType
    private var _list: List<RaceModel> = emptyList()
    val list: List<RaceModel>
        get() = _list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolderType: RaceAdapterViewHolderType? = viewType.toEnum<RaceAdapterViewHolderType>()
        val view = if (viewHolderType != null) {
            LayoutInflater.from(parent.context).inflate(viewHolderType.viewHolderRes, parent, false)
        } else {
            View(parent.context)
        }
        return when (viewHolderType) {
            RaceAdapterViewHolderType.RACE_PODIUM -> RacePodiumViewHolder(view)
            RaceAdapterViewHolderType.RACE_RESULT -> RaceResultViewHolder(view)
            RaceAdapterViewHolderType.RACE_RESULT_HEADER -> RaceResultHeaderViewHolder(view)
            else -> throw Error("View type not implemented")
        }
    }

    override fun getItemCount(): Int = when (viewType) {
        RaceAdapterType.RACE -> list.size - 1
        RaceAdapterType.QUALIFYING_POS,
        RaceAdapterType.QUALIFYING_POS_1,
        RaceAdapterType.QUALIFYING_POS_2,
        RaceAdapterType.QUALIFYING_POS_3 -> list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            RaceAdapterType.RACE -> when (position) {
                0 -> RaceAdapterViewHolderType.RACE_PODIUM.ordinal
                1 -> RaceAdapterViewHolderType.RACE_RESULT_HEADER.ordinal
                else -> RaceAdapterViewHolderType.RACE_RESULT.ordinal
            }
            RaceAdapterType.QUALIFYING_POS -> RaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            RaceAdapterType.QUALIFYING_POS_1 -> RaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            RaceAdapterType.QUALIFYING_POS_2 -> RaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            RaceAdapterType.QUALIFYING_POS_3 -> RaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
        }
    }

    fun update(type: RaceAdapterType, list: List<RaceModel>) {
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
        when (getItemViewType(position).toEnum<RaceAdapterViewHolderType>()) {
            RaceAdapterViewHolderType.RACE_PODIUM -> {
                (holder as? RacePodiumViewHolder)?.bind(list[0], list[1], list[2])
            }
            RaceAdapterViewHolderType.RACE_RESULT -> {
                (holder as? RaceResultViewHolder)?.bind(list[position + 1])
            }
            else -> {}
        }
    }

    //region Diff calculator

    inner class DiffCalculator(
        private val oldList: List<RaceModel>,
        private val newList: List<RaceModel>,
        private val oldType: RaceAdapterType,
        private val newType: RaceAdapterType
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].driver.id == newList[newItemPosition].driver.id

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = false
    }

    //endregion
}


enum class RaceAdapterType {
    RACE,
    QUALIFYING_POS_1,
    QUALIFYING_POS_2,
    QUALIFYING_POS_3,
    QUALIFYING_POS,
}

enum class RaceAdapterViewHolderType(
    @LayoutRes val viewHolderRes: Int
) {
    RACE_PODIUM(R.layout.view_race_podium),
    RACE_RESULT(R.layout.view_race_result),
    RACE_RESULT_HEADER(R.layout.view_race_header),
    QUALIFYING_RESULT_HEADER(R.layout.view_qualifying_header),
    QUALIFYING_RESULT(R.layout.view_qualifying_result),
}

interface RaceAdapterCallback {
    fun orderBy(adapterType: RaceAdapterType)
}