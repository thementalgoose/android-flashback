package tmg.flashback.googleanalytics.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import javax.inject.Inject

interface ScreenViewViewModelInputs {
    fun viewScreen(name: String, args: Map<String, String>)
}

interface ScreenViewViewModelOutputs

@HiltViewModel
class ScreenViewViewModel @Inject constructor(
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager,
): ViewModel(), ScreenViewViewModelInputs, ScreenViewViewModelOutputs {

    val inputs: ScreenViewViewModelInputs = this
    val outputs: ScreenViewViewModelOutputs = this

    override fun viewScreen(name: String, args: Map<String, String>) {
        firebaseAnalyticsManager.viewScreen(name, args)
    }
}