package tmg.flashback.ads.usecases

import tmg.flashback.ads.repository.AdsCacheRepository
import javax.inject.Inject

internal class ClearCachedAdvertsUseCase @Inject constructor(
    val repository: AdsCacheRepository
) {
    fun clear() {
        repository.listOfAds.forEach {
            it.destroy()
        }
        repository.listOfAds = emptyList()
    }
}