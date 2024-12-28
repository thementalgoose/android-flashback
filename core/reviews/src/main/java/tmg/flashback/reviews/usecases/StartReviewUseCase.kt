package tmg.flashback.reviews.usecases

import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import tmg.flashback.device.ActivityProvider
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.reviews.manager.AppReviewManager
import tmg.flashback.reviews.repository.AppReviewRepository
import javax.inject.Inject

class StartReviewUseCase @Inject constructor(
    private val topActivityProvider: ActivityProvider,
    private val appReviewManager: AppReviewManager,
    private val analyticsManager: FirebaseAnalyticsManager,
    private val appReviewRepository: AppReviewRepository,
) {

    suspend fun start() {

    }
}