package tmg.flashback.stats.ui.messaging

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.repository.models.Banner
import tmg.flashback.web.WebNavigationComponent
import javax.inject.Inject

interface BannerViewModelInputs {
    fun navigateToWeb(url: String)
}

interface BannerViewModelOutputs {
    val message: Banner?
}

@HiltViewModel
class BannerViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val webNavigationComponent: WebNavigationComponent
): ViewModel(), BannerViewModelInputs, BannerViewModelOutputs {

    val inputs: BannerViewModelInputs = this
    val outputs: BannerViewModelOutputs = this

    override val message: Banner? get() = homeRepository.banner

    override fun navigateToWeb(url: String) {
        webNavigationComponent.web(url)
    }
}