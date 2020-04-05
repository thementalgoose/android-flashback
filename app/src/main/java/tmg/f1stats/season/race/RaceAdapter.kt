package tmg.f1stats.season.race

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.f1stats.R
import tmg.f1stats.season.race.viewholders.QualifyingResultViewHolder
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
            RaceAdapterViewHolderType.QUALIFYING_RESULT -> QualifyingResultViewHolder(view, callback)
            else -> throw Error("View type not implemented")
        }
    }

    override fun getItemCount(): Int = when (viewType) {
        RaceAdapterType.RACE -> list.size
        RaceAdapterType.QUALIFYING_POS,
        RaceAdapterType.QUALIFYING_POS_1,
        RaceAdapterType.QUALIFYING_POS_2 -> list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            RaceAdapterType.RACE -> when (position) {
                0 -> RaceAdapterViewHolderType.RACE_PODIUM.ordinal
                else -> RaceAdapterViewHolderType.RACE_RESULT.ordinal
            }
            RaceAdapterType.QUALIFYING_POS -> RaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            RaceAdapterType.QUALIFYING_POS_1 -> RaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            RaceAdapterType.QUALIFYING_POS_2 -> RaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
        }
    }

    fun update(type: RaceAdapterType, list: List<RaceModel>) {

        val result = DiffUtil.calculateDiff(DiffCalculator(
            oldList = this.list,
            newList = list,
            oldType = this._viewType,
            newType = type
        ))
        this._viewType = type
        this._list = list
        result.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position).toEnum<RaceAdapterViewHolderType>()) {
            RaceAdapterViewHolderType.RACE_PODIUM -> {
                val viewHolder = holder as RacePodiumViewHolder
                val model = list[position] as RaceModel.Podium
                viewHolder.bind(model.driverFirst, model.driverSecond, model.driverThird)
            }
            RaceAdapterViewHolderType.RACE_RESULT -> {
                val viewHolder = holder as RaceResultViewHolder
                viewHolder.bind(list[position] as RaceModel.Single)
            }
            RaceAdapterViewHolderType.QUALIFYING_RESULT -> {
                val viewHolder = holder as QualifyingResultViewHolder
                viewHolder.bind(list[position] as RaceModel.Single)
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

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

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