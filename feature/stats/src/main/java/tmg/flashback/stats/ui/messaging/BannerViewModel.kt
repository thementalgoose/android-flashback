package tmg.flashback.stats.ui.messaging

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.repository.models.Banner
import tmg.flashback.web.usecases.OpenWebpageUseCase
import javax.inject.Inject

interface BannerViewModelInputs {
    fun navigateToWeb(url: String)
}

interface BannerViewModelOutputs {
    val message: List<Banner>
}

@HiltViewModel
class BannerViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val openWebpageUseCase: OpenWebpageUseCase
): ViewModel(), BannerViewModelInputs, BannerViewModelOutputs {

    val inputs: BannerViewModelInputs = this
    val outputs: BannerViewModelOutputs = this

    override val message: List<Banner> get() {
        return homeRepository.banners
    }

    override fun navigateToWeb(url: String) {
        openWebpageUseCase.open(url, title = "")
    }
}