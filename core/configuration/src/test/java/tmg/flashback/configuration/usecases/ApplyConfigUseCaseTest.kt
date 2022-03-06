package tmg.flashback.configuration.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService

internal class ApplyConfigUseCaseTest {

    private val mockConfigService: RemoteConfigService = mockk(relaxed = true)
    private val mockConfigRepository: ConfigRepository = mockk(relaxed = true)

    private lateinit var underTest: ApplyConfigUseCase

    private fun initUnderTest() {
        underTest = ApplyConfigUseCase(
            mockConfigService,
            mockConfigRepository
        )
    }

    @Test
    fun `remote config sync updated if activate succeeds`() {
        coEvery { mockConfigService.activate() } returns true

        initUnderTest()
        runBlocking {
            underTest.apply()
        }

        coVerify {
            mockConfigService.activate()
        }
        verify {
            mockConfigRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }

    @Test
    fun `remote config sync not updated if activate fails`() {
        coEvery { mockConfigService.activate() } returns false

        initUnderTest()
        runBlocking {
            underTest.apply()
        }

        coVerify {
            mockConfigService.activate()
        }
        verify(exactly = 0) {
            mockConfigRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }
}