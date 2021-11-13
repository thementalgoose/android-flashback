package tmg.flashback.statistics.ui.race

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.component.KoinApiExtension
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.RaceQualifyingType
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.*
import tmg.flashback.statistics.ui.race.viewholders.*
import tmg.flashback.statistics.ui.race_old.RaceAdapterType
import tmg.flashback.statistics.ui.race_old.RaceModel
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class RaceAdapter(
    private val pillItemClicked: (PillItem) -> Unit,
    private val driverClicked: (driver: Driver) -> Unit,
    private val constructorClicked: (constructor: Constructor) -> Unit,
    private val orderBy: (raceQualifyingType: RaceQualifyingType) -> Unit,
): SyncAdapter<RaceItem>() {

    override var list: List<RaceItem> = listOf(RaceItem.PodiumLoading)
        set(value) {
            val sourceList = value.addDataProvidedByItem()
            val result = DiffUtil.calculateDiff(DiffCalculator(
                oldList = field,
                newList = sourceList
            ))
            field = sourceList
            result.dispatchUpdatesTo(this)
        }

    override fun dataProvidedItem(syncDataItem: SyncDataItem): RaceItem = RaceItem.ErrorItem(syncDataItem)

    override val providedByAtTopIndex: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_race_overview -> OverviewViewHolder(
                pillItemClicked,
                ViewRaceOverviewBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_race_podium -> RacePodiumViewHolder(
                driverClicked,
                ViewRaceRacePodiumBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_race_result -> RaceResultViewHolder(
                driverClicked,
                ViewRaceRaceResultBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_sprint_qualifying_result -> RaceSprintQualifyingViewHolder(
                driverClicked,
                ViewRaceSprintQualifyingResultBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_qualifying_result -> QualifyingResultViewHolder(
                orderBy,
                ViewRaceQualifyingResultBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_race_header -> RaceResultHeaderViewHolder(
                ViewRaceRaceHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_qualifying_header -> QualifyingHeaderViewHolder(
                orderBy,
                ViewRaceQualifyingHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_constructor -> ConstructorStandingsViewholder(
                constructorClicked,
                ViewRaceConstructorBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.skeleton_race -> SkeletonLoadingViewHolder(
                SkeletonRaceBinding.inflate(layoutInflater, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is RaceItem.Overview -> (holder as OverviewViewHolder).bind(item)

            is RaceItem.Constructor -> (holder as ConstructorStandingsViewholder).bind(item)

            is RaceItem.Podium -> (holder as RacePodiumViewHolder).bind(item.driverFirst, item.driverSecond, item.driverThird)
            is RaceItem.RaceResult -> (holder as RaceResultViewHolder).bind(item)

            is RaceItem.QualifyingHeader -> (holder as QualifyingHeaderViewHolder).bind(item)
            is RaceItem.QualifyingResult -> (holder as QualifyingResultViewHolder).bind(item)
            is RaceItem.SprintQualifyingResult -> (holder as RaceSprintQualifyingViewHolder).bind(item)

            is RaceItem.ErrorItem -> super.bindErrors(holder, item.item)
        }
    }

    override fun viewType(position: Int): Int = list[position].layoutId

    inner class DiffCalculator(
        private val oldList: List<RaceItem>,
        private val newList: List<RaceItem>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(old: Int, new: Int): Boolean = oldList[old] == newList[new] || isOverview(oldList[old], newList[new])

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(o: Int, n: Int): Boolean = areItemsTheSame(o, n) && !isOverview(oldList[o], newList[n])

        private fun isOverview(old: RaceItem, new: RaceItem) = old is RaceItem.Overview && new is RaceItem.Overview
    }

}