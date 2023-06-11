package tmg.flashback.ads.adsadmob.manager

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.formats.NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.adsadmob.BuildConfig
import tmg.flashback.ads.adsadmob.R
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine

@Singleton
class AdsManager @Inject constructor(
    private val adsRepository: AdsRepository
) {
    companion object {
        private const val numberOfAdsToLoad = 4
    }

    fun initialize(context: Context) {
        MobileAds.initialize(context)
        adsRepository.getCurrentDeviceId(context)?.let {
            Log.d("Adverts", "Adding debug test device id of $it")
            RequestConfiguration.Builder().setTestDeviceIds(listOf(it))
        }
    }

    suspend fun getNativeAd(context: Context): List<NativeAd> {
        val list = mutableListOf<NativeAd>()
        return suspendCoroutine { continuation ->
            log("getNativeAd builder")

            var builder: AdLoader? = null
            builder = AdLoader.Builder(context, getNativeAdId(context))
                .forNativeAd { nativeAd ->
                    log("forNativeAd ${nativeAd.headline} - ${nativeAd.body}")
                    list.add(nativeAd)
                    if (builder?.isLoading != true) {
                        log("forNativeAd < $list")
                        continuation.resumeWith(Result.success(list))
                    }
                }
                .withNativeAdOptions(NativeAdOptions
                    .Builder()
                    .setMediaAspectRatio(NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE)
                    .build()
                )
                .withAdListener(object : AdListener() {
                    override fun onAdClosed() {
                        super.onAdClosed()
                        log("onAdClosed")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        log("onAdClicked")
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        log("onAdImpression")
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        log("onAdOpened")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        log("onAdFailedToLoad < $list")
                        log(error.toString())
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        log("onAdLoaded")
                    }
                })
                .build()

            builder.loadAds(AdRequest.Builder().build(), numberOfAdsToLoad)
        }
    }

    private fun getNativeAdId(context: Context): String {
        return context.getString(R.string.admob_ad_unit_schedule_id)
    }

    private fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i("Adverts", msg)
        }
    }
}