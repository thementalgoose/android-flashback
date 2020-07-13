package tmg.flashback.race

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.race.viewholders.*
import tmg.flashback.shared.viewholders.DataUnavailableViewHolder
import tmg.flashback.shared.viewholders.NoNetworkViewHolder
import tmg.utilities.extensions.toEnum

class RaceAdapter(
    private val callback: RaceAdapterCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewType: RaceAdapterType =
        RaceAdapterType.RACE
        private set
    var list: List<RaceAdapterModel> = listOf(RaceAdapterModel.Loading)
        private set

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
            RaceAdapterViewHolderType.QUALIFYING_RESULT_HEADER -> QualifyingHeaderViewHolder(view, callback)
            RaceAdapterViewHolderType.QUALIFYING_RESULT -> QualifyingResultViewHolder(view, callback)
            RaceAdapterViewHolderType.CONSTRUCTOR_STANDINGS -> ConstructorStandingsViewholder(view)
            RaceAdapterViewHolderType.NO_NETWORK -> NoNetworkViewHolder(view)
            RaceAdapterViewHolderType.DATA_UNAVAILABLE -> DataUnavailableViewHolder(view)
            RaceAdapterViewHolderType.LOADING -> SkeletonLoadingViewHolder(view)
            else -> throw Error("View type not implemented")
        }
    }

    override fun getItemCount(): Int = when (viewType) {
        RaceAdapterType.RACE -> list.size
        RaceAdapterType.CONSTRUCTOR_STANDINGS,
        RaceAdapterType.QUALIFYING_POS,
        RaceAdapterType.QUALIFYING_POS_1,
        RaceAdapterType.QUALIFYING_POS_2 -> list.size
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position] is RaceAdapterModel.Unavailable) {
            return RaceAdapterViewHolderType.DATA_UNAVAILABLE.ordinal
        }
        if (list[position] is RaceAdapterModel.NoNetwork) {
            return RaceAdapterViewHolderType.NO_NETWORK.ordinal
        }
        if (list[position] is RaceAdapterModel.Loading) {
            return RaceAdapterViewHolderType.LOADING.ordinal
        }
        return when (viewType) {
            RaceAdapterType.RACE -> when (position) {
                0 -> RaceAdapterViewHolderType.RACE_PODIUM.ordinal
                1 -> RaceAdapterViewHolderType.RACE_RESULT_HEADER.ordinal
                else -> RaceAdapterViewHolderType.RACE_RESULT.ordinal
            }
            RaceAdapterType.QUALIFYING_POS,
            RaceAdapterType.QUALIFYING_POS_1,
            RaceAdapterType.QUALIFYING_POS_2 -> when {
                list[position] is RaceAdapterModel.QualifyingHeader -> {
                    RaceAdapterViewHolderType.QUALIFYING_RESULT_HEADER.ordinal
                }
                else -> {
                    RaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
                }
            }
            RaceAdapterType.CONSTRUCTOR_STANDINGS -> RaceAdapterViewHolderType.CONSTRUCTOR_STANDINGS.ordinal
        }
    }

    fun update(type: RaceAdapterType, list: List<RaceAdapterModel>) {

        val result = DiffUtil.calculateDiff(
            DiffCalculator(
                oldList = this.list,
                newList = list,
                oldType = this.viewType,
                newType = type
            )
        )
        this.viewType = type
        this.list = list
        result.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DataUnavailableViewHolder -> {
                holder.bind((list[position] as RaceAdapterModel.Unavailable).type)
            }
            is NoNetworkViewHolder -> { }
            else -> {
                when (getItemViewType(position).toEnum<RaceAdapterViewHolderType>()) {
                    RaceAdapterViewHolderType.RACE_PODIUM -> {
                        val viewHolder = holder as RacePodiumViewHolder
                        val model = list[position] as RaceAdapterModel.Podium
                        viewHolder.bind(model.driverFirst, model.driverSecond, model.driverThird)
                    }
                    RaceAdapterViewHolderType.RACE_RESULT -> {
                        val viewHolder = holder as RaceResultViewHolder
                        viewHolder.bind(list[position] as RaceAdapterModel.Single)
                    }
                    RaceAdapterViewHolderType.QUALIFYING_RESULT_HEADER -> {
                        val viewHolder = holder as QualifyingHeaderViewHolder
                        viewHolder.bind(
                            (list[position] as RaceAdapterModel.QualifyingHeader).showQualifyingDeltas,
                            viewType
                        )
                    }
                    RaceAdapterViewHolderType.QUALIFYING_RESULT -> {
                        val viewHolder = holder as QualifyingResultViewHolder
                        viewHolder.bind(list[position] as RaceAdapterModel.Single, viewType)
                    }
                    RaceAdapterViewHolderType.CONSTRUCTOR_STANDINGS -> {
                        val viewHolder = holder as ConstructorStandingsViewholder
                        viewHolder.bind(
                            list[position] as RaceAdapterModel.ConstructorStandings,
                            maxPointsByAnyTeam = maxPointsByAnyTeam()
                        )
                    }
                }
            }
        }
    }

    private fun maxPointsByAnyTeam(): Int {
        val defaultPoints = 20
        return list
            .map { (it as? RaceAdapterModel.ConstructorStandings)?.points ?: defaultPoints }
            .max() ?: defaultPoints
    }

    //region Diff calculator

    inner class DiffCalculator(
        private val oldList: List<RaceAdapterModel>,
        private val newList: List<RaceAdapterModel>,
        private val oldType: RaceAdapterType,
        private val newType: RaceAdapterType
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(old: Int, new: Int): Boolean = oldList[old] == newList[new]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(o: Int, n: Int): Boolean = areItemsTheSame(o, n) && oldType == newType
    }

    //endregion
}


enum class RaceAdapterType {
    RACE,
    QUALIFYING_POS_1,
    QUALIFYING_POS_2,
    QUALIFYING_POS,
    CONSTRUCTOR_STANDINGS
}

fun RaceAdapterType.isQualifying(): Boolean {
    return this == RaceAdapterType.QUALIFYING_POS || this == RaceAdapterType.QUALIFYING_POS_1 || this == RaceAdapterType.QUALIFYING_POS_2
}

enum class RaceAdapterViewHolderType(
    @LayoutRes val viewHolderRes: Int
) {
    RACE_PODIUM(R.layout.view_race_race_podium),
    RACE_RESULT(R.layout.view_race_race_result),
    RACE_RESULT_HEADER(R.layout.view_race_race_header),
    QUALIFYING_RESULT_HEADER(R.layout.view_race_qualifying_header),
    QUALIFYING_RESULT(R.layout.view_race_qualifying_result),
    CONSTRUCTOR_STANDINGS(R.layout.view_race_constructor),
    NO_NETWORK(R.layout.view_shared_no_network),
    DATA_UNAVAILABLE(R.layout.view_shared_data_unavailable),
    LOADING(R.layout.skeleton_race)
}

interface RaceAdapterCallback {
    fun orderBy(adapterType: RaceAdapterType)
}