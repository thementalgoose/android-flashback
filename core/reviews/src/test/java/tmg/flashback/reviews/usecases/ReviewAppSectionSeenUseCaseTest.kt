package tmg.flashback.reviews.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.reviews.repository.AppReviewRepository
import tmg.flashback.reviews.usecases.AppSection.DETAILS_RACE
import tmg.flashback.reviews.usecases.AppSection.HOME

internal class ReviewAppSectionSeenUseCaseTest {

    private val mockAppReviewRepository: AppReviewRepository = mockk(relaxed = true)

    private lateinit var underTest: ReviewAppSectionSeenUseCase

    private fun initUnderTest() {
        underTest = ReviewAppSectionSeenUseCase(
            appReviewRepository = mockAppReviewRepository
        )
    }

    @Test
    fun `setting section marks section in repo`() {
        every { mockAppReviewRepository.sectionsSeen } returns setOf(HOME)

        initUnderTest()
        underTest.invoke(DETAILS_RACE)

        verify {
            mockAppReviewRepository.sectionsSeen
            mockAppReviewRepository.sectionsSeen = setOf(HOME, DETAILS_RACE)
        }
    }
}