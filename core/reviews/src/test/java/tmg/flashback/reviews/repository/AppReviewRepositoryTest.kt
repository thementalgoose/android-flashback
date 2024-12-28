package tmg.flashback.reviews.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.reviews.usecases.AppSection
import tmg.flashback.reviews.usecases.AppSection.DETAILS_RACE
import tmg.flashback.reviews.usecases.AppSection.HOME

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

    @Test
    fun `sections returns empty value for invalid entries`() {
        every { mockPreferenceManager.getSet(KEY_SECTIONS, emptySet()) } returns mutableSetOf("random", "info")

        initUnderTest()
        assertEquals(emptySet<AppSection>(), underTest.sectionsSeen)
    }

    @Test
    fun `sections returns mapped value for valid entries`() {
        every { mockPreferenceManager.getSet(KEY_SECTIONS, emptySet()) } returns mutableSetOf(HOME.key, DETAILS_RACE.key)

        initUnderTest()
        assertEquals(setOf(HOME, DETAILS_RACE), underTest.sectionsSeen)
    }

    @Test
    fun `sections save values`() {
        every { mockPreferenceManager.getSet(KEY_SECTIONS, emptySet()) } returns mutableSetOf(HOME.key)

        initUnderTest()
        underTest.sectionsSeen += HOME
        verify { mockPreferenceManager.save(KEY_SECTIONS, setOf(HOME.key)) }

        underTest.sectionsSeen += DETAILS_RACE
        verify { mockPreferenceManager.save(KEY_SECTIONS, setOf(HOME.key, DETAILS_RACE.key)) }
    }


    companion object {
        private const val KEY_PROMPTED_REVIEW = "review_prompted"
        private const val KEY_SECTIONS = "review_sections"
    }
}