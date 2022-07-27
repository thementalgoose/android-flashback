package tmg.flashback.ads.usecases

import tmg.flashback.ads.repository.AdsCacheRepository

internal class ClearCachedAdvertsUseCase(
    val repository: AdsCacheRepository
) {
    fun clear() {
        repository.listOfAds.forEach {
            it.destroy()
        }
        repository.listOfAds = emptyList()
    }
}