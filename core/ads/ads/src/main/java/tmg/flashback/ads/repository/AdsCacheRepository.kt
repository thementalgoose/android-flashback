package tmg.flashback.ads.repository

import com.google.android.gms.ads.nativead.NativeAd
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsCacheRepository @Inject constructor() {

    internal var listOfAds: List<NativeAd> = emptyList()
}