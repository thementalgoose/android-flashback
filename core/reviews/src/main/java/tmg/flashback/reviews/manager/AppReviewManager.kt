package tmg.flashback.reviews.manager

import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppReviewManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
) {
    internal val manager = ReviewManagerFactory.create(applicationContext)
}