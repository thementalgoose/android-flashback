package tmg.flashback.overviews.constructor

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.overviews.constructor.summary.ConstructorSummaryItem
import tmg.flashback.repo.db.stats.ConstructorDB
import tmg.flashback.repo.models.stats.ConstructorOverview
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.utils.position
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface ConstructorViewModelInputs {
    fun setup(constructorId: String)
    fun openUrl(url: String)
    fun openSeason(season: Int)
}

//endregion

//region Outputs

interface ConstructorViewModelOutputs {
    val list: LiveData<List<ConstructorSummaryItem>>
    val openUrl: LiveData<DataEvent<String>>
    val openSeason: LiveData<DataEvent<Pair<String, Int>>>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class ConstructorViewModel(
        private val constructorDB: ConstructorDB,
        private val connectivityManager: ConnectivityManager,
        scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), ConstructorViewModelInputs, ConstructorViewModelOutputs {

    var inputs: ConstructorViewModelInputs = this
    var outputs: ConstructorViewModelOutputs = this

    private val constructorId: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()

    override val openUrl: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val openSeason: MutableLiveData<DataEvent<Pair<String, Int>>> = MutableLiveData()

    init {

    }

    //region Inputs

    override fun setup(constructorId: String) {
        if (this.constructorId.valueOrNull != constructorId) {
            this.constructorId.offer(constructorId)
        }
    }

    override fun openUrl(url: String) {
        openUrl.postValue(DataEvent(url))
    }

    override fun openSeason(season: Int) {
        openSeason.postValue(DataEvent(Pair(constructorId.value, season)))
    }

    //endregion

    //region Outputs

    //endregion

    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(overview: ConstructorOverview): List<ConstructorSummaryItem> {
        val list: MutableList<ConstructorSummaryItem> = mutableListOf()
        list.addStat(
                tint = if (overview.championships > 0) R.attr.f1Favourite else R.attr.f1TextSecondary,
                icon = R.drawable.ic_menu_drivers,
                label = R.string.driver_overview_stat_career_drivers_title,
                value = overview.championships.toString()
        )

        overview.championshipWins?.let {
            list.addStat(
                    icon = R.drawable.ic_championship_order,
                    label = R.string.driver_overview_stat_career_best_championship_position,
                    value = it.ordinalAbbreviation
            )
        }

        list.addStat(
                icon = R.drawable.ic_standings,
                label = R.string.driver_overview_stat_career_wins,
                value = overview.totalWins.toString()
        )
        list.addStat(
                icon = R.drawable.ic_podium,
                label = R.string.driver_overview_stat_career_podiums,
                value = overview.totalPodiums.toString()
        )
        list.addStat(
                icon = R.drawable.ic_status_finished,
                label = R.string.driver_overview_stat_career_best_finish,
                value = overview.bestFinish.position()
        )
        list.addStat(
                icon = R.drawable.ic_finishes_in_points,
                label = R.string.driver_overview_stat_career_points_finishes,
                value = overview.finishesInPoints.toString()
        )
        list.addStat(
                icon = R.drawable.ic_race_points,
                label = R.string.driver_overview_stat_career_points,
                value = overview.totalPoints.toString()
        )
        list.addStat(
                icon = R.drawable.ic_qualifying_pole,
                label = R.string.driver_overview_stat_career_qualifying_pole,
                value = overview.careerQualifyingPoles.toString()
        )
        list.addStat(
                icon = R.drawable.ic_qualifying_front_row,
                label = R.string.driver_overview_stat_career_qualifying_top_3,
                value = overview.careerQualifyingTop3.toString()
        )
        list.addStat(
                icon = R.drawable.ic_qualifying_top_ten,
                label = R.string.driver_overview_stat_career_qualifying_top_10,
                value = overview.totalQualifyingAbove(10).toString()
        )

        return list
    }

    private fun MutableList<ConstructorSummaryItem>.addStat(@AttrRes tint: Int = R.attr.f1TextSecondary, @DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(
                ConstructorSummaryItem.Stat(
                        tint = tint,
                        icon = icon,
                        label = label,
                        value = value
                ))
    }
}
