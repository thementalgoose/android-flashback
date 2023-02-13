package tmg.flashback.web

import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.web.usecases.OpenWebpageUseCase
import javax.inject.Inject

@Deprecated("Please use OpenWebpageUseCase instead", replaceWith = ReplaceWith("OpenWebpageUseCase"))
class WebNavigationComponent @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val openWebpageUseCase: OpenWebpageUseCase
) {
    fun web(url: String, title: String = "") = activityProvider.launch {
        openWebpageUseCase.open(it, url, title)
    }
}