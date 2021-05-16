package tmg.common.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.common.constants.Releases
import tmg.common.repository.ReleaseNotesRepository
import tmg.core.device.managers.BuildConfigManager

internal class ReleaseNotesControllerTest {

    private var mockReleaseNotesRepository: ReleaseNotesRepository = mockk(relaxed = true)
    private var mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var sut: ReleaseNotesController

    private fun initSUT() {
        sut = ReleaseNotesController(mockReleaseNotesRepository, mockBuildConfigManager)
    }

    @Test
    fun `major release version not found if no change in version code`() {
        every { mockBuildConfigManager.versionCode } returns 30
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 30
        initSUT()

        assertEquals(emptyList<Releases>(), sut.majorReleaseNotes)
    }

    @Test
    fun `major release version not found no release notes between last and now are major`() {
        every { mockBuildConfigManager.versionCode } returns 27
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 22
        initSUT()

        assertEquals(emptyList<Releases>(), sut.majorReleaseNotes)
    }

    @Test
    fun `major release version found when major found between last and now`() {
        every { mockBuildConfigManager.versionCode } returns 30
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 27
        initSUT()

        assertEquals(listOf(Releases.VERSION_28), sut.majorReleaseNotes)
    }

    @Test
    fun `major release version doesn't include last version`() {
        every { mockBuildConfigManager.versionCode } returns 31
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 28
        initSUT()

        assertEquals(listOf(Releases.VERSION_31), sut.majorReleaseNotes)
    }

    @Test
    fun `major release version only takes 3 latest items when multiple found`() {
        every { mockBuildConfigManager.versionCode } returns 40
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 27
        initSUT()

        assertEquals(
            listOf(Releases.VERSION_40, Releases.VERSION_32, Releases.VERSION_31),
            sut.majorReleaseNotes
        )
    }

    @Test
    fun `when release notes last seen version is 0 then mark release notes called`() {
        every { mockBuildConfigManager.versionCode } returns 20
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 0
        initSUT()
        val throwaway = sut.majorReleaseNotes
        verify {
            mockReleaseNotesRepository.releaseNotesSeenAppVersion = 20
        }
    }

    @Test
    fun `mark release notes seen saves current version in prefs`() {
        every { mockBuildConfigManager.versionCode } returns 20
        initSUT()

        sut.markReleaseNotesSeen()
        verify {
            mockReleaseNotesRepository.releaseNotesSeenAppVersion = 20
        }
    }
}