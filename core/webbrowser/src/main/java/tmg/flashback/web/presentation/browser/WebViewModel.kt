package tmg.flashback.web.presentation.browser

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.device.usecases.OpenUrlUseCase
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.flashback.web.usecases.ShareUseCase
import javax.inject.Inject

interface WebViewModelInputs {
    fun openWebpage(url: String)
    fun openShare(url: String)
}

interface WebViewModelOutputs {

}

@HiltViewModel
class WebViewModel @Inject constructor(
    private val openWebpageUseCase: OpenUrlUseCase,
    private val shareUseCase: ShareUseCase
): ViewModel(), WebViewModelInputs, WebViewModelOutputs {

    val inputs: WebViewModelInputs = this
    val outputs: WebViewModelOutputs = this

    override fun openWebpage(url: String) {
        openWebpageUseCase.openUrl(url)
    }

    override fun openShare(url: String) {
        shareUseCase.shareUrl(url)
    }
}