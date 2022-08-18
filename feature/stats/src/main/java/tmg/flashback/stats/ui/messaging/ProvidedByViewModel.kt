package tmg.flashback.stats.ui.messaging

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import javax.inject.Inject

interface ProvidedByViewModelInputs {
    fun navigateToAboutApp()
}

interface ProvidedByViewModelOutputs {
    val message: String?
}

@HiltViewModel
class ProvidedByViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val navigationComponent: ApplicationNavigationComponent
): ViewModel(), ProvidedByViewModelInputs, ProvidedByViewModelOutputs {

    val inputs: ProvidedByViewModelInputs = this
    val outputs: ProvidedByViewModelOutputs = this

    override val message: String? get() = homeRepository.dataProvidedBy

    override fun navigateToAboutApp() {
        navigationComponent.aboutApp()
    }
}