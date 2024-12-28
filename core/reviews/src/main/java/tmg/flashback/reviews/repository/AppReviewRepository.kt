package tmg.flashback.reviews.repository

import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.reviews.usecases.AppSection
import tmg.utilities.extensions.toEnum
import javax.inject.Inject

class AppReviewRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    internal var hasPromptedForReview: Boolean
        get() = preferenceManager.getBoolean(KEY_PROMPTED_REVIEW, false)
        set(value) = preferenceManager.save(KEY_PROMPTED_REVIEW, value)

    internal var sectionsSeen: Set<AppSection>
        get() = preferenceManager
            .getSet(KEY_SECTIONS, emptySet())
            .mapNotNull { value -> value.toEnum<AppSection> { it.key } }
            .toSet()
        set(value) = preferenceManager.save(KEY_SECTIONS, value.map { it.key }.toSet())

    companion object {
        private const val KEY_PROMPTED_REVIEW = "review_prompted"
        private const val KEY_SECTIONS = "review_sections"
    }
}