package tmg.flashback.ui.dashboard2.medium

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface DashboardMediumViewModelInputs {

}

interface DashboardMediumViewModelOutputs {

}

@HiltViewModel
class DashboardMediumViewModel @Inject constructor(): ViewModel(), DashboardMediumViewModelInputs,
    DashboardMediumViewModelOutputs {

    val inputs: DashboardMediumViewModelInputs = this
    val outputs: DashboardMediumViewModelOutputs = this

}