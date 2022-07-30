package tmg.flashback.ui.components.analytics

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.analytics.manager.AnalyticsManager
import javax.inject.Inject

interface ScreenViewViewModelInputs {
    fun viewScreen(name: String, args: Map<String, String>)
}

interface ScreenViewViewModelOutputs {
}

@HiltViewModel
class ScreenViewViewModel @Inject constructor(
    private val analyticsManager: AnalyticsManager
): ViewModel(), ScreenViewViewModelInputs, ScreenViewViewModelOutputs {

    val inputs: ScreenViewViewModelInputs = this
    val outputs: ScreenViewViewModelOutputs = this

    override fun viewScreen(name: String, args: Map<String, String>) {
        analyticsManager.viewScreen(name, args)
    }
}