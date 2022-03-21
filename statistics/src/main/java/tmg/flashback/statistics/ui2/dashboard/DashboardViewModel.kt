package tmg.flashback.statistics.ui2.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    override val currentTab: MutableLiveData<DashboardScreenState> = MutableLiveData()

    override fun clickSeason(season: Int) {

    }

    override fun clickTab(tab: DashboardNavItem) {
        currentTab.postValue(DashboardScreenState(
            tab = tab,
            season = currentTab.value?.season ?:
        ))
    }
}