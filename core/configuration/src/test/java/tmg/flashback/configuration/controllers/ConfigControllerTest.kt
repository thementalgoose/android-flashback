package tmg.flashback.configuration.controllers

import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.testutils.BaseTest

internal class ConfigControllerTest: BaseTest() {

    private val mockConfigService: RemoteConfigService = mockk(relaxed = true)
    private val mockConfigRepository: ConfigRepository = mockk(relaxed = true)

    private lateinit var underTest: ConfigController

    private fun initUnderTest() {
        underTest = ConfigController(mockConfigRepository, mockConfigService)
    }

    @BeforeEach
    internal fun setUp() {
        coEvery { mockConfigService.activate() } returns true
    }

    //region ensure cache reset check

    @Test
    fun `ensure cache reset check calls reset if existing version doesnt match migrations`() {

        every { mockConfigRepository.resetAtMigrationVersion } returns Migrations.configurationSyncCount - 1

        initUnderTest()
        runBlockingTest {
            underTest.ensureCacheReset()
        }
        verify { mockConfigRepository.resetAtMigrationVersion = Migrations.configurationSyncCount }
        coVerify { mockConfigService.reset() }
    }

    @Test
    fun `ensure cache check doesnt call reset if existing version matches match migrations`() {
        every { mockConfigRepository.resetAtMigrationVersion } returns Migrations.configurationSyncCount

        initUnderTest()
        runBlockingTest {
            underTest.ensureCacheReset()
        }
        verify(exactly = 0) { mockConfigRepository.resetAtMigrationVersion = Migrations.configurationSyncCount }
        coVerify(exactly = 0) { mockConfigService.reset() }
    }

    //endregion

    //region Fetching / updating logic

    @Test
    fun `fetch calls update in manager`() {
        initUnderTest()
        runBlockingTest {
            underTest.fetch()
        }

        coVerify {
            mockConfigService.fetch(false)
        }
    }

    @Test
    fun `fetch and apply calls update in manager and saves remote config sync`() {
        coEvery { mockConfigService.fetch(true) } returns true
        initUnderTest()
        runBlockingTest {
            underTest.fetchAndApply()
        }

        coVerify {
            mockConfigService.fetch(true)
        }
        verify {
            mockConfigRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }

    @Test
    fun `fetch and apply calls update in manager but doesnt saves remote config sync if not successful fetch`() {
        coEvery { mockConfigService.fetch(true) } returns false
        initUnderTest()
        runBlockingTest {
            underTest.fetchAndApply()
        }

        coVerify {
            mockConfigService.fetch(true)
        }
        verify(exactly = 0) {
            mockConfigRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }

    @Test
    fun `apply pending calls manager`() = coroutineTest {
        initUnderTest()
        runBlockingTest {
            underTest.applyPending()
        }

        coVerify {
            mockConfigService.activate()
        }
    }

    //endregion

    //region Reset

    @Test
    fun `resetting config sets require sync to 0 and calls reset`() = coroutineTest {
        initUnderTest()
        runBlockingTest {
            underTest.reset()
        }

        coVerify {
            mockConfigService.reset()
            mockConfigRepository.remoteConfigSync = 0
        }
    }

    //endregion
}