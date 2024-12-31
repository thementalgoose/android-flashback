package tmg.flashback.reviews.usecases

import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.ActivityProvider
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.reviews.manager.AppReviewManager
import tmg.flashback.reviews.repository.AppReviewRepository
import javax.inject.Inject

class StartReviewUseCase @Inject constructor(
    private val topActivityProvider: ActivityProvider,
    private val appReviewManager: AppReviewManager,
    private val crashlyticsManager: CrashlyticsManager,
    private val analyticsManager: FirebaseAnalyticsManager,
    private val appReviewRepository: AppReviewRepository,
) {

    suspend fun start() {
        try {
            if (!appReviewRepository.hasPromptedForReview && appReviewRepository.sectionsSeen == AppSection.entries.toSet()) {
                crashlyticsManager.log("Starting app review prompt")
                val reviewInfo = appReviewManager.requestReview()
                val activity = topActivityProvider.activity
                if (activity != null) {
                    analyticsManager.logEvent("review_prompt_request")
                    appReviewRepository.hasPromptedForReview = true
                    appReviewManager.launchReview(activity, reviewInfo)
                    analyticsManager.logEvent("review_prompt_complete")
                } else {
                    crashlyticsManager.log("Top activity not available")
                }
            }
        } catch (e: Exception) {
            crashlyticsManager.logException(ReviewNotPromptedException(e))
        }
    }
}

internal class ReviewNotPromptedException(cause: Exception): Exception(cause)