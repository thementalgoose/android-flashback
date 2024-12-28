package tmg.flashback.reviews.repository

import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject

class AppReviewRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    internal var hasPromptedForReview: Boolean
        get() = preferenceManager.getBoolean(KEY_PROMPTED_REVIEW, false)
        set(value) = preferenceManager.save(KEY_PROMPTED_REVIEW, value)

    companion object {
        private const val KEY_PROMPTED_REVIEW = "prompted_review"
    }
}