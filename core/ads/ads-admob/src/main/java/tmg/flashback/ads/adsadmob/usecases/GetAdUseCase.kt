package tmg.flashback.ads.adsadmob.usecases

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.nativead.NativeAd
import tmg.flashback.ads.adsadmob.BuildConfig
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.adsadmob.manager.AdsManager
import tmg.flashback.ads.adsadmob.repository.AdsCacheRepository
import javax.inject.Inject

internal class GetAdUseCase @Inject constructor(
    private val adsRepository: AdsRepository,
    private val adsCacheRepository: AdsCacheRepository,
    private val adsManager: AdsManager
) {
    suspend fun getAd(context: Context, index: Int = 0): NativeAd? {
        if (!adsRepository.isEnabled) {
            return null
        }

        var listOfAds = adsCacheRepository.listOfAds
        if (listOfAds.isNotEmpty()) {
            Log.d(
                "Adverts",
                "Requesting display of advert ${index}. Ads list size is ${listOfAds.size} (${index % listOfAds.size})"
            )
            return listOfAds
                .filterIndexed { i, _ ->
                    (index % listOfAds.size) == i
                }
                .firstOrNull()
        }

        listOfAds = try {
            listOfAds.forEach { it.destroy() }
            adsManager.getNativeAd(context)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
                Log.e("Adverts", "Failed to load native ads, ${e.message}")
            }
            emptyList()
        }
        adsCacheRepository.listOfAds = listOfAds

        if (listOfAds.isEmpty()) return null

        Log.d("Adverts", "Initial data ${index}. Ads list size is ${listOfAds.size} (${index % listOfAds.size})")
        return listOfAds
            .filterIndexed { i, _ ->
                (index % listOfAds.size) == i
            }
            .firstOrNull()
    }
}