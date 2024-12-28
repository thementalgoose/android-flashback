package tmg.flashback.reviews.usecases

import tmg.flashback.reviews.repository.AppReviewRepository
import javax.inject.Inject

class ReviewAppSectionSeenUseCase @Inject constructor(
    private val appReviewRepository: AppReviewRepository
) {
    operator fun invoke(section: AppSection) {
        appReviewRepository.sectionsSeen += section
    }
}

enum class AppSection(
    internal val key: String
) {
    HOME("home"),
    DETAILS_QUALIFYING("details_qualifying"),
    DETAILS_RACE("details_race");
}