package tmg.flashback.statistics.ui.race

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.*
import tmg.flashback.statistics.ui.race.viewholders.*
import tmg.flashback.statistics.ui.race.viewholders.qualifying.QualifyingResultQ1Q2Q3ViewHolder
import tmg.flashback.statistics.ui.race.viewholders.qualifying.QualifyingResultQ1Q2ViewHolder
import tmg.flashback.statistics.ui.race.viewholders.qualifying.QualifyingResultQ1ViewHolder
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class RaceAdapter(
    private val pillItemClicked: (PillItem) -> Unit,
    private val driverClicked: (driver: Driver) -> Unit,
    private val constructorClicked: (constructor: Constructor) -> Unit
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
            R.layout.view_race_qualifying_q1q2q3_result -> QualifyingResultQ1Q2Q3ViewHolder(
                driverClicked,
                ViewRaceQualifyingQ1q2q3ResultBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_qualifying_q1q2_result -> QualifyingResultQ1Q2ViewHolder(
                driverClicked,
                ViewRaceQualifyingQ1q2ResultBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_qualifying_q1_result -> QualifyingResultQ1ViewHolder(
                driverClicked,
                ViewRaceQualifyingQ1ResultBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_race_header -> RaceResultHeaderViewHolder(
                ViewRaceRaceHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_qualifying_header -> QualifyingHeaderViewHolder(
                ViewRaceQualifyingHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_constructor -> ConstructorStandingsViewholder(
                constructorClicked,
                ViewRaceConstructorBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_schedule_list -> ScheduleItemViewHolder(
                ViewRaceScheduleListBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_race_advert -> AdvertViewHolder(
                ViewRaceAdvertBinding.inflate(layoutInflater, parent, false)
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

            is RaceItem.ScheduleMax -> (holder as ScheduleItemViewHolder).bind(item)

            is RaceItem.Constructor -> (holder as ConstructorStandingsViewholder).bind(item)

            is RaceItem.Podium -> (holder as RacePodiumViewHolder).bind(item.driverFirst, item.driverSecond, item.driverThird)
            is RaceItem.RaceResult -> (holder as RaceResultViewHolder).bind(item)

            is RaceItem.QualifyingHeader -> (holder as QualifyingHeaderViewHolder).bind(item)
            is RaceItem.QualifyingResultQ1Q2Q3 -> (holder as QualifyingResultQ1Q2Q3ViewHolder).bind(item)
            is RaceItem.QualifyingResultQ1Q2 -> (holder as QualifyingResultQ1Q2ViewHolder).bind(item)
            is RaceItem.QualifyingResultQ1 -> (holder as QualifyingResultQ1ViewHolder).bind(item)
            is RaceItem.SprintQualifyingResult -> (holder as RaceSprintQualifyingViewHolder).bind(item)

            is RaceItem.ErrorItem -> super.bindErrors(holder, item.item)

            else -> { }
        }
    }

    override fun viewType(position: Int): Int = list[position].layoutId

    inner class DiffCalculator(
        private val oldList: List<RaceItem>,
        private val newList: List<RaceItem>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(old: Int, new: Int): Boolean = oldList[old].id == newList[new].id || isOverview(oldList[old], newList[new])

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(o: Int, n: Int): Boolean = oldList[o] == newList[n] || isOverviewContents(oldList[o], newList[n])

        private fun isOverview(old: RaceItem, new: RaceItem) = old is RaceItem.Overview && new is RaceItem.Overview
        private fun isOverviewContents(old: RaceItem, new: RaceItem) = old is RaceItem.Overview && new is RaceItem.Overview &&
                old.wikipedia == new.wikipedia && old.laps == new.laps
    }

}