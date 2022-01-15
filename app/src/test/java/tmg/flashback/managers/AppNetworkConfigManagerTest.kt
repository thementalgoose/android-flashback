package tmg.flashback.managers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.repositories.NetworkConfigRepository

internal class AppNetworkConfigManagerTest {

    private val mockNetworkConfigRepository: NetworkConfigRepository = mockk(relaxed = true)

    private lateinit var sut: AppNetworkConfigManager

    private fun initSUT() {
        sut = AppNetworkConfigManager(mockNetworkConfigRepository)
    }

    @Test
    fun `base url calls network config repository`() {
        every { mockNetworkConfigRepository.configUrl } returns "sample"
        initSUT()
        assertEquals("sample", sut.baseUrl)
        verify {
            mockNetworkConfigRepository.configUrl
        }
    }
}