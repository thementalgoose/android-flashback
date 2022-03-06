package tmg.flashback.configuration.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService

internal class FetchConfigUseCaseTest {

    private val mockConfigService: RemoteConfigService = mockk(relaxed = true)
    private val mockConfigRepository: ConfigRepository = mockk(relaxed = true)

    private lateinit var underTest: FetchConfigUseCase

    private fun initUnderTest() {
        underTest = FetchConfigUseCase(
            mockConfigService,
            mockConfigRepository
        )
    }

    @Test
    fun `fetch calls fetch false`() {
        coEvery { mockConfigService.fetch(any()) } returns true

        initUnderTest()
        runBlocking {
            underTest.fetch()
        }

        coVerify {
            mockConfigService.fetch(false)
        }
        verify(exactly = 0) {
            mockConfigRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }

    @Test
    fun `fetch calls fetch true and updates remote config sync when fetch succeeds`() {
        coEvery { mockConfigService.fetch(any()) } returns true

        initUnderTest()
        runBlocking {
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
    fun `fetch calls fetch true and updates remote config sync when fetch fails`() {
        coEvery { mockConfigService.fetch(any()) } returns false

        initUnderTest()
        runBlocking {
            underTest.fetchAndApply()
        }

        coVerify {
            mockConfigService.fetch(true)
        }
        verify(exactly = 0) {
            mockConfigRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
    }
}