package tmg.flashback.ads.adsadmob.usecases

import tmg.flashback.ads.adsadmob.repository.AdsCacheRepository
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