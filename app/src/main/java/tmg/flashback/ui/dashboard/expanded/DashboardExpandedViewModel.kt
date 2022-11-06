package tmg.flashback.ui.dashboard.expanded

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ui.dashboard.MenuSeasonItem
import javax.inject.Inject

interface DashboardExpandedViewModelInputs {
    fun clickNavItem(navItem: DashboardExpandedNavItem)
    fun clickSeason(season: Int)
}

interface DashboardExpandedViewModelOutputs {
    val navigation: LiveData<List<DashboardExpandedNavItem>>
    val seasons: LiveData<List<MenuSeasonItem>>
}

@HiltViewModel
class DashboardExpandedViewModel @Inject constructor(): ViewModel(), DashboardExpandedViewModelInputs, DashboardExpandedViewModelOutputs {

    val inputs: DashboardExpandedViewModelInputs = this
    val outputs: DashboardExpandedViewModelOutputs = this

    override val navigation: MutableLiveData<List<DashboardExpandedNavItem>> = MutableLiveData()
    override val seasons: MutableLiveData<List<MenuSeasonItem>> = MutableLiveData()

    override fun clickNavItem(navItem: DashboardExpandedNavItem) {

    }

    override fun clickSeason(season: Int) {

    }
}