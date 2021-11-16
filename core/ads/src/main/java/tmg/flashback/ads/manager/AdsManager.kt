package tmg.flashback.ads.manager

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.formats.NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE
import com.google.android.gms.ads.formats.OnAdManagerAdViewLoadedListener
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import tmg.flashback.ads.BuildConfig
import tmg.flashback.ads.R
import tmg.utilities.extensions.md5
import java.util.*
import kotlin.coroutines.suspendCoroutine

class AdsManager(
    context: Context
) {

    companion object {
        private const val numberOfAdsToLoad = 1
    }

    // TODO: Remove this from here and move it to FlashbackApplication
    init {
        Log.d("Adverts", "Initializing mobile ads SDK")
        initialize(context)
    }

    fun initialize(context: Context) {
        MobileAds.initialize(context)
        getCurrentDeviceId(context)?.let {
            Log.d("Adverts", "Adding debug test device id of $it")
            RequestConfiguration.Builder().setTestDeviceIds(listOf(it))
        }
    }

    suspend fun getNativeAd(context: Context): List<NativeAd> {
        val list = mutableListOf<NativeAd>()
        return suspendCoroutine { continuation ->
            @Suppress("ObjectLiteralToLambda")
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

    /**
     * Get the current admob device test id
     *  This is the same ID used by admob for test device id!
     */
    private fun getCurrentDeviceId(context: Context): String? {
        if (BuildConfig.DEBUG) {
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val deviceId = androidId.md5.uppercase()
            log( "Current Device Id found to be $deviceId")
            return deviceId
        }
        return null
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