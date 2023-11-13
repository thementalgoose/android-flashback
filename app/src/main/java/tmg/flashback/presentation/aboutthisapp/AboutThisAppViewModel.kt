package tmg.flashback.presentation.aboutthisapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.aboutthisapp.configuration.Configuration
import tmg.flashback.device.usecases.CopyToClipboardUseCase
import tmg.flashback.device.usecases.OpenPlayStoreUseCase
import tmg.flashback.device.usecases.OpenSendEmailUseCase
import tmg.flashback.repositories.ContactRepository
import tmg.flashback.web.usecases.OpenWebpageUseCase
import javax.inject.Inject

@HiltViewModel
class AboutThisAppViewModel @Inject constructor(
    private val aboutThisAppConfigProvider: AboutThisAppConfigProvider,
    private val contactRepository: ContactRepository,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val openPlayStoreUseCase: OpenPlayStoreUseCase,
    private val openSendEmailUseCase: OpenSendEmailUseCase,
    private val copyToClipboardUseCase: CopyToClipboardUseCase,
): ViewModel() {

    private val _config: MutableStateFlow<Configuration> = MutableStateFlow(aboutThisAppConfigProvider.getConfig())
    val config: StateFlow<Configuration> = _config

    fun openUrl(string: String) {
        openWebpageUseCase.open(string, "")
    }

    fun openPlaystore() {
        openPlayStoreUseCase.openPlaystore()
    }

    fun openEmail() {
        openSendEmailUseCase.sendEmail(contactRepository.contactEmail)
    }

    fun copyToClipboard(content: String) {
        copyToClipboardUseCase.copyToClipboard(content)
    }
}