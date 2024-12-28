package tmg.flashback.reviews.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.reviews.repository.AppReviewRepository
import tmg.flashback.reviews.usecases.AppSection.DETAILS_RACE
import tmg.flashback.reviews.usecases.AppSection.HOME_CALENDAR

internal class ReviewSectionSeenUseCaseTest {

    private val mockAppReviewRepository: AppReviewRepository = mockk(relaxed = true)

    private lateinit var underTest: ReviewSectionSeenUseCase

    private fun initUnderTest() {
        underTest = ReviewSectionSeenUseCase(
            appReviewRepository = mockAppReviewRepository
        )
    }

    @Test
    fun `setting section marks section in repo`() {
        every { mockAppReviewRepository.sectionsSeen } returns setOf(HOME_CALENDAR)

        initUnderTest()
        underTest.invoke(DETAILS_RACE)

        verify {
            mockAppReviewRepository.sectionsSeen
            mockAppReviewRepository.sectionsSeen = setOf(HOME_CALENDAR, DETAILS_RACE)
        }
    }
}