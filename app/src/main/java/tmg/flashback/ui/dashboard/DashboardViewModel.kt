package tmg.flashback.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.SeasonConstructorStandings
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase

//region Inputs

interface DashboardViewModelInputs {
    fun clickTab(tab: DashboardNavItem)
    fun clickSeason(season: Int)

    fun clickRace(overviewRace: OverviewRace)
    fun clickDriver(seasonDriverStanding: SeasonDriverStandings)
    fun clickConstructor(seasonConstructorStanding: SeasonConstructorStandings)
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val currentTab: LiveData<DashboardScreenState>

    val subContent: LiveData<SideContentView?>
}

//endregion

class DashboardViewModel(
    private val defaultSeasonUseCase: DefaultSeasonUseCase
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    private val defaultTab: DashboardNavItem = DashboardNavItem.CALENDAR

    override val subContent: MutableLiveData<SideContentView> = MutableLiveData()
    override val currentTab: MutableLiveData<DashboardScreenState> = MutableLiveData(DashboardScreenState(
        tab = defaultTab,
        season = defaultSeasonUseCase.defaultSeason
    ))

    override fun clickSeason(season: Int) {
        currentTab.postValue(DashboardScreenState(
            tab = currentTab.value?.tab ?: defaultTab,
            season = season
        ))
    }

    override fun clickTab(tab: DashboardNavItem) {
        currentTab.postValue(DashboardScreenState(
            tab = tab,
            season = currentTab.value?.season ?: defaultSeasonUseCase.defaultSeason
        ))
    }

    override fun clickRace(overviewRace: OverviewRace) {
        subContent.value = SideContentView.Race
    }

    override fun clickConstructor(seasonConstructorStanding: SeasonConstructorStandings) {
        subContent.value = SideContentView.Constructor
    }

    override fun clickDriver(seasonDriverStanding: SeasonDriverStandings) {
        subContent.value = SideContentView.Driver
    }
}