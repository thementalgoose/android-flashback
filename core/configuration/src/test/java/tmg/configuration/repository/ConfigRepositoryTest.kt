package tmg.configuration.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.core.prefs.manager.PreferenceManager

internal class ConfigRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: ConfigRepository

    private fun initSUT() {
        sut = ConfigRepository(mockPreferenceManager)
    }

    //region Remote Config Sync count

    @Test
    fun `remote config sync count calls shared prefs repository`() {
        every { mockPreferenceManager.getInt(keyRemoteConfigSync, any()) } returns 3
        initSUT()
        assertEquals(sut.remoteConfigSync, 3)
        verify {
            mockPreferenceManager.getInt(keyRemoteConfigSync, 0)
        }
    }

    @Test
    fun `remote config sync count saves in shared prefs repository`() {
        initSUT()
        sut.remoteConfigSync = 2
        verify {
            mockPreferenceManager.save(keyRemoteConfigSync, 2)
        }
    }

    //endregion

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"
    }
}