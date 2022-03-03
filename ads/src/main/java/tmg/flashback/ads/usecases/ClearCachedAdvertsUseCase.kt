package tmg.flashback.ads.usecases

import tmg.flashback.ads.repository.AdsRepository

internal class ClearCachedAdvertsUseCase(
    val repository: AdsRepository
) {
    fun clear() {
        repository.listOfAds.forEach {
            it.destroy()
        }
        repository.listOfAds = emptyList()
    }
}