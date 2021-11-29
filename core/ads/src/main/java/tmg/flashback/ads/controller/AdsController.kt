package tmg.flashback.ads.controller

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.nativead.NativeAd
import tmg.flashback.ads.BuildConfig
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.repository.AdsRepository

class AdsController(
    private val repository: AdsRepository,
    private val manager: AdsManager
) {

    private var listOfAds: List<NativeAd> = emptyList()

    /**
     * What is the users preference for enabling adverts
     */
    var userConfigAdvertsEnabled: Boolean
        get() = repository.userPrefEnabled
        set(value) {
            repository.userPrefEnabled = value
        }

    /**
     * Allow the user to adjust their advert configuration
     */
    val allowUserConfig: Boolean by lazy {
        repository.allowUserConfig
    }

    /**
     * Are adverts enabled or not based off the configuration
     */
    val areAdvertsEnabled: Boolean by lazy { repository.isEnabled }

    /**
     * Initialise the ad manager and any test device ids for debug mode
     */
    fun initialise(context: Context) {
        manager.initialize(context)
    }

    /**
     * Clear advert cache and force refresh on next ad retreival
     */
    fun clearCachedAdverts() {
        listOfAds.forEach {
            it.destroy()
        }
    }

    suspend fun getAd(context: Context): NativeAd? {
        if (!repository.isEnabled) {
            return null
        }
        if (listOfAds.isNotEmpty()) {
            return listOfAds.firstOrNull()
        }

        try {
            listOfAds.forEach { it.destroy() }
            listOfAds = manager.getNativeAd(context)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
                Log.e("Adverts", "Failed to load native ads, ${e.message}")
            }
        }
        return listOfAds.firstOrNull()
    }
}