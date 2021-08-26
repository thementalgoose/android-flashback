package tmg.configuration.controllers

import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.constants.Migrations
import tmg.configuration.services.RemoteConfigService
import tmg.configuration.repository.ConfigRepository

internal class ConfigControllerTest {

    private val mockConfigService: RemoteConfigService = mockk(relaxed = true)
    private val mockConfigRepository: ConfigRepository = mockk(relaxed = true)

    private lateinit var sut: ConfigController

    private fun initSUT() {
        sut = ConfigController(mockConfigRepository, mockConfigService)
    }

    @Test
    fun `on init set defaults is called`() {

        initSUT()

        verify {
            mockConfigService.initialiseRemoteConfig()
        }
    }

    //region Require sync

    @Test
    fun `require synchronisation reads value from remote config sync`() {

        every { mockConfigRepository.remoteConfigSync } returns 0
        initSUT()

        assertTrue(sut.requireSynchronisation)
        verify {
            mockConfigRepository.remoteConfigSync
        }
    }

    @Test
    fun `require synchronisation returns false when migrations match`() {

        every { mockConfigRepository.remoteConfigSync } returns Migrations.configurationSyncCount
        initSUT()

        assertFalse(sut.requireSynchronisation)
        verify {
            mockConfigRepository.remoteConfigSync
        }
    }

    //endregion

    //region reset check

    @Test
    fun `reset check calls reset if existing version doesnt match migrations`() {

        every { mockConfigRepository.resetAtMigrationVersion } returns Migrations.configurationSyncCount - 1

        initSUT()
        verify { mockConfigRepository.resetAtMigrationVersion = Migrations.configurationSyncCount }
        coVerify { mockConfigService.reset() }
    }

    @Test
    fun `reset check doesnt call reset if existing version matches match migrations`() {
        every { mockConfigRepository.resetAtMigrationVersion } returns Migrations.configurationSyncCount

        initSUT()
        verify(exactly = 0) { mockConfigRepository.resetAtMigrationVersion = Migrations.configurationSyncCount }
        coVerify(exactly = 0) { mockConfigService.reset() }
    }

    //endregion

    //region Fetching / updating logic

    @Test
    fun `fetch calls update in manager`() {
        initSUT()
        runBlockingTest {
            sut.fetch()
        }

        coVerify {
            mockConfigService.fetch(false)
        }
    }

    @Test
    fun `fetch and apply calls update in manager and saves remote config sync`() {
        coEvery { mockConfigService.fetch(true) } returns true
        initSUT()
        runBlockingTest {
            sut.fetchAndApply()
        }

        coVerify {
            mockConfigService.fetch(true)
        }
        verify {
            mockConfigRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }

    @Test
    fun `apply pending calls manager`() {
        initSUT()
        runBlockingTest {
            sut.applyPending()
        }

        coVerify {
            mockConfigService.activate()
        }
    }

    //endregion
}