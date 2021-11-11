package tmg.flashback.common.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class ReleaseNotesRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk()

    private lateinit var sut: ReleaseNotesRepository

    private fun initSUT() {
        sut = ReleaseNotesRepository(mockPreferenceManager)
    }

    @Test
    fun `remote config sync count calls shared prefs repository`() {
        every { mockPreferenceManager.getInt(keyReleaseNotesSeenVersion, any()) } returns 3
        initSUT()
        assertEquals(3, sut.releaseNotesSeenAppVersion)
        verify {
            mockPreferenceManager.getInt(keyReleaseNotesSeenVersion, 0)
        }
    }

    @Test
    fun `remote config sync count saves in shared prefs repository`() {
        every { mockPreferenceManager.save(keyReleaseNotesSeenVersion, any<Int>()) } returns Unit
        initSUT()
        sut.releaseNotesSeenAppVersion = 2
        verify {
            mockPreferenceManager.save(keyReleaseNotesSeenVersion, 2)
        }
    }

    companion object {
        private const val keyReleaseNotesSeenVersion: String = "RELEASE_NOTES_SEEN_VERSION"
    }
}