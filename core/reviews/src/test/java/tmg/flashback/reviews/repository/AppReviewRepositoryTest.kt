package tmg.flashback.reviews.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class AppReviewRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: AppReviewRepository

    private fun initUnderTest() {
        underTest = AppReviewRepository(
            preferenceManager = mockPreferenceManager
        )
    }

    @Test
    fun `has prompted for review saves key`() {
        initUnderTest()

        underTest.hasPromptedForReview = true
        verify {
            mockPreferenceManager.save(KEY_PROMPTED_REVIEW, true)
        }
    }

    @Test
    fun `has prompted for review gets key`() {
        every { mockPreferenceManager.getBoolean(KEY_PROMPTED_REVIEW, false) } returns true

        initUnderTest()

        assertEquals(true, mockPreferenceManager.getBoolean(KEY_PROMPTED_REVIEW, false))
    }


    companion object {
        private const val KEY_PROMPTED_REVIEW = "prompted_review"
    }
}