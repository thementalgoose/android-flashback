package tmg.flashback.ads.adsadmob.repository

import com.google.android.gms.ads.nativead.NativeAd
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AdsCacheRepository @Inject constructor() {

    internal var listOfAds: List<NativeAd> = emptyList()
}