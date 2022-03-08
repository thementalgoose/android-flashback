package tmg.flashback.configuration.usecases

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.services.RemoteConfigService

internal class InitialiseConfigUseCaseTest {

    private val mockConfigService: RemoteConfigService = mockk(relaxed = true)

    private lateinit var underTest: InitialiseConfigUseCase

    private fun initUnderTest() {
        underTest = InitialiseConfigUseCase(
            mockConfigService
        )
    }

    @Test
    fun `initialise calls remote config service`() {
        initUnderTest()
        underTest.initialise()

        verify {
            mockConfigService.initialiseRemoteConfig()
        }
    }
}