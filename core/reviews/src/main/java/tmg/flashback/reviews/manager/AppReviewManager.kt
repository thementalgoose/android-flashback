package tmg.flashback.reviews.manager

import android.app.Activity
import android.content.Context
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppReviewManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
) {
    private val manager = ReviewManagerFactory.create(applicationContext)

    suspend fun requestReview(): ReviewInfo {
        return manager.requestReview()
    }

    suspend fun launchReview(activity: Activity, reviewInfo: ReviewInfo) {
        manager.launchReview(activity, reviewInfo)
    }
}