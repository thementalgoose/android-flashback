package tmg.flashback.common.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.prefs.manager.PreferenceManager

internal class ReleaseNotesRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk()
    private val mockBuildConfigManager: BuildConfigManager = mockk()

    private lateinit var sut: ReleaseNotesRepository

    private fun initSUT() {
        sut = ReleaseNotesRepository(mockPreferenceManager, mockBuildConfigManager)
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
        every { mockBuildConfigManager.versionCode } returns 2
        initSUT()
        sut.releaseNotesSeen()
        verify {
            mockPreferenceManager.save(keyReleaseNotesSeenVersion, 2)
            mockBuildConfigManager.versionCode
        }
    }

    companion object {
        private const val keyReleaseNotesSeenVersion: String = "RELEASE_NOTES_SEEN_VERSION"
    }
}