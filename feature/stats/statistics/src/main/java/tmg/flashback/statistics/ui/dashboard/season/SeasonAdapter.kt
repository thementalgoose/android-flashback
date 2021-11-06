package tmg.flashback.statistics.ui.dashboard.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.component.inject
import tmg.core.ui.controllers.ThemeController
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.*
import tmg.flashback.statistics.ui.dashboard.season.viewholders.*
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class SeasonAdapter(
    private val trackClicked: (track: SeasonItem.Track) -> Unit,
    private val calendarWeekRaceClicked: (track: SeasonItem.CalendarWeek) -> Unit,
    private val driverClicked: (driver: SeasonItem.Driver) -> Unit,
    private val constructorClicked: (constructor: SeasonItem.Constructor) -> Unit
): SyncAdapter<SeasonItem>() {

    private val themeController: ThemeController by inject()

    override var list: List<SeasonItem> = emptyList()
        set(initialValue) {
            val value = initialValue.addDataProvidedByItem()
            val result = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun dataProvidedItem(syncDataItem: SyncDataItem) = SeasonItem.ErrorItem(syncDataItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_dashboard_season_track -> TrackViewHolder(
                trackClicked,
                ViewDashboardSeasonTrackBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_dashboard_season_driver -> DriverViewHolder(
                driverClicked,
                ViewDashboardSeasonDriverBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_dashboard_season_constructor -> ConstructorViewHolder(
                constructorClicked,
                ViewDashboardSeasonConstructorBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_dashboard_season_calendar_header -> CalendarHeaderViewHolder(
                ViewDashboardSeasonCalendarHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_dashboard_season_calendar_month -> CalendarMonthViewHolder(
                ViewDashboardSeasonCalendarMonthBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_dashboard_season_calendar_week -> CalendarWeekViewHolder(
                ViewDashboardSeasonCalendarWeekBinding.inflate(layoutInflater, parent, false),
                calendarWeekRaceClicked,
                themeController.animationSpeed
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is SeasonItem.Track -> (holder as TrackViewHolder).bind(item)
            is SeasonItem.Driver -> (holder as DriverViewHolder).bind(item)
            is SeasonItem.Constructor -> (holder as ConstructorViewHolder).bind(item)
            is SeasonItem.ErrorItem -> bindErrors(holder, item.item)
            is SeasonItem.CalendarMonth -> (holder as CalendarMonthViewHolder).bind(item)
            is SeasonItem.CalendarWeek -> (holder as CalendarWeekViewHolder).bind(item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId

    inner class DiffCallback(
        private val oldList: List<SeasonItem>,
        private val newList: List<SeasonItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(o: Int, n: Int) = oldList[o] == newList[n] ||
                areTracksTheSame(o, n)

        override fun areContentsTheSame(o: Int, n: Int) = oldList[o] == newList[n]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        private fun areTracksTheSame(old: Int, new: Int): Boolean {
            val oldItem = oldList[old] as? SeasonItem.Track
            val newItem = newList[new] as? SeasonItem.Track
            if (oldItem != null && newItem != null) {
                return oldItem.raceName == newItem.raceName &&
                        oldItem.raceCountryISO == newItem.raceCountryISO
            }
            return false
        }
    }
}