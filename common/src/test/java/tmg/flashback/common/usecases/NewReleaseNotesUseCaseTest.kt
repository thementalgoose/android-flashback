package tmg.flashback.common.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.common.constants.ReleaseNotes
import tmg.flashback.common.controllers.ReleaseNotesController
import tmg.flashback.common.repository.ReleaseNotesRepository
import tmg.flashback.device.managers.BuildConfigManager

internal class NewReleaseNotesUseCaseTest {

    private var mockReleaseNotesRepository: ReleaseNotesRepository = mockk(relaxed = true)
    private var mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var sut: NewReleaseNotesUseCase

    private fun initSUT() {
        sut = NewReleaseNotesUseCase(mockReleaseNotesRepository, mockBuildConfigManager)
    }

    @Test
    fun `major release version not found if no change in version code`() {
        every { mockBuildConfigManager.versionCode } returns 30
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 30
        initSUT()

        assertEquals(emptyList<ReleaseNotes>(), sut.getNotes())
    }

    @Test
    fun `major release version not found no release notes between last and now are major`() {
        every { mockBuildConfigManager.versionCode } returns 27
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 22
        initSUT()

        assertEquals(emptyList<ReleaseNotes>(), sut.getNotes())
    }

    @Test
    fun `major release version found when major found between last and now`() {
        every { mockBuildConfigManager.versionCode } returns 5
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 3
        initSUT()

        assertEquals(listOf(ReleaseNotes.VERSION_4), sut.getNotes())
    }

    @Test
    fun `major release version doesn't include last version`() {
        every { mockBuildConfigManager.versionCode } returns 4
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 2
        initSUT()

        assertEquals(listOf(ReleaseNotes.VERSION_4), sut.getNotes())
    }

    @Test
    fun `when release notes last seen version is 0 then mark release notes called`() {
        every { mockBuildConfigManager.versionCode } returns 20
        every { mockReleaseNotesRepository.releaseNotesSeenAppVersion } returns 0
        initSUT()
        val throwaway = sut.getNotes()
        verify {
            mockReleaseNotesRepository.releaseNotesSeen()
        }
    }
}