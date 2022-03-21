package tmg.flashback.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase

//region Inputs

interface DashboardViewModelInputs {
    fun clickTab(tab: DashboardNavItem)
    fun clickSeason(season: Int)
}

//endregion

//region Outputs

interface DashboardViewModelOutputs {
    val currentTab: LiveData<DashboardScreenState>
}

//endregion

class DashboardViewModel(
    private val defaultSeasonUseCase: DefaultSeasonUseCase
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    private val defaultTab: DashboardNavItem = DashboardNavItem.CALENDAR

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
}