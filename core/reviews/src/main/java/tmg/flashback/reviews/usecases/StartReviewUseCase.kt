package tmg.flashback.reviews.usecases

import android.util.Log
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.ActivityProvider
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.reviews.BuildConfig
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
            if (appReviewRepository.hasPromptedForReview) {
                crashlyticsManager.log("App review prompt - Previously prompted for review, not requesting")
                return
            }
            if (appReviewRepository.sectionsSeen != AppSection.entries.toSet()) {
                crashlyticsManager.log("App review prompt - Only ${appReviewRepository.sectionsSeen.size} seen, not requesting")
                return
            }

            if (BuildConfig.DEBUG) {
                Log.d("Review", "App Review Prompt, sectionsSeen=${appReviewRepository.sectionsSeen}, hasPrompted=${appReviewRepository.hasPromptedForReview}")
            }

            crashlyticsManager.log("App review prompt - Requesting review")
            val reviewInfo = appReviewManager.requestReview()
            val activity = topActivityProvider.activity
            if (activity != null) {
                analyticsManager.logEvent("review_prompt_request")
                appReviewRepository.hasPromptedForReview = true
                appReviewManager.launchReview(activity, reviewInfo)
                analyticsManager.logEvent("review_prompt_complete")
            } else {
                crashlyticsManager.log("App review prompt - Not displaying due to top activity not available")
            }
        } catch (e: Exception) {
            crashlyticsManager.logException(ReviewNotPromptedException(e))
        }
    }
}

internal class ReviewNotPromptedException(cause: Exception): Exception(cause)