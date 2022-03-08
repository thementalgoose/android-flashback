package tmg.flashback.configuration.usecases

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService

internal class ResetConfigUseCaseTest {

    private val mockConfigService: RemoteConfigService = mockk(relaxed = true)
    private val mockConfigRepository: ConfigRepository = mockk(relaxed = true)

    private lateinit var underTest: ResetConfigUseCase

    private fun initUnderTest() {
        underTest = ResetConfigUseCase(
            mockConfigService,
            mockConfigRepository
        )
    }

    @Test
    fun `reset calls config service reset`() {
        initUnderTest()
        runBlocking {
            underTest.reset()
        }

        verify {
            mockConfigRepository.remoteConfigSync = 0
        }
        coVerify {
            mockConfigService.reset()
        }
    }

    @Test
    fun `ensure reset calls reset if existing does match sync count`() {
        every { mockConfigRepository.resetAtMigrationVersion } returns Migrations.configurationSyncCount

        initUnderTest()
        runBlocking {
            underTest.ensureReset()
        }

        verify(exactly = 0) {
            mockConfigRepository.remoteConfigSync = any()
        }
        coVerify(exactly = 0) {
            mockConfigService.reset()
        }
    }

    @Test
    fun `ensure reset doesnt calls reset if sync catch count`() {
        every { mockConfigRepository.resetAtMigrationVersion } returns (Migrations.configurationSyncCount - 1)

        initUnderTest()
        runBlocking {
            underTest.ensureReset()
        }

        verify {
            mockConfigRepository.resetAtMigrationVersion = Migrations.configurationSyncCount
        }
        coVerify {
            mockConfigService.reset()
        }
    }
}