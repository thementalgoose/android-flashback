package tmg.flashback.race

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.race.viewholders.*
import tmg.flashback.shared.sync.SyncAdapter

class RaceAdapter(
    private val callback: RaceAdapterCallback
) : SyncAdapter<RaceModel>() {

    var viewType: RaceAdapterType =
        RaceAdapterType.RACE
        private set

    override var list: List<RaceModel> = listOf(RaceModel.Loading)

    fun update(type: RaceAdapterType, list: List<RaceModel>) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.view_race_race_podium -> RacePodiumViewHolder(callback::driverClicked, inflatedView)
            R.layout.view_race_race_result -> RaceResultViewHolder(callback::driverClicked, inflatedView)
            R.layout.view_race_race_header -> RaceResultHeaderViewHolder(inflatedView)
            R.layout.view_race_qualifying_header -> QualifyingHeaderViewHolder(inflatedView, callback)
            R.layout.view_race_qualifying_result -> QualifyingResultViewHolder(inflatedView, callback)
            R.layout.view_race_constructor -> ConstructorStandingsViewholder(callback::constructorClicked, inflatedView)
            R.layout.skeleton_race -> SkeletonLoadingViewHolder(inflatedView)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun viewType(position: Int): Int {
        return when (val item = list[position]) {
            is RaceModel.Single -> item.layoutIdByViewType(viewType)
            else -> item.layoutId
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is RaceModel.Podium -> (holder as RacePodiumViewHolder).bind(item.driverFirst, item.driverSecond, item.driverThird)
            is RaceModel.Single -> {
                when (viewType) {
                    RaceAdapterType.RACE -> (holder as RaceResultViewHolder).bind(item)
                    else -> (holder as QualifyingResultViewHolder).bind(item, viewType)
                }
            }
            is RaceModel.RaceHeader -> { }
            is RaceModel.QualifyingHeader -> (holder as QualifyingHeaderViewHolder).bind(item.showQualifyingDeltas, viewType)
            is RaceModel.ConstructorStandings -> (holder as ConstructorStandingsViewholder).bind(item, maxPointsByAnyTeam())
            is RaceModel.ErrorItem -> super.bindErrors(holder, item.item)
        }
    }

    // TODO: Look at moving this out to the view model
    private fun maxPointsByAnyTeam(): Int {
        val defaultPoints = 20
        return list
                .map { (it as? RaceModel.ConstructorStandings)?.points ?: defaultPoints }
                .maxOrNull() ?: defaultPoints
    }

    //region Diff calculator

    inner class DiffCalculator(
            private val oldList: List<RaceModel>,
            private val newList: List<RaceModel>,
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

interface RaceAdapterCallback {
    fun orderBy(adapterType: RaceAdapterType)
    fun driverClicked(driverId: String, driverName: String)
    fun constructorClicked(constructorId: String, constructorName: String)
}