package tmg.flashback.configuration.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class ConfigRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: ConfigRepository

    private fun initUnderTest() {
        underTest = ConfigRepository(mockPreferenceManager)
    }

    //region Remote Config Sync count

    @Test
    fun `remote config sync count calls shared prefs repository`() {
        every { mockPreferenceManager.getInt(keyRemoteConfigSync, any()) } returns 3
        initUnderTest()
        assertEquals(underTest.remoteConfigSync, 3)
        verify {
            mockPreferenceManager.getInt(keyRemoteConfigSync, 0)
        }
    }

    @Test
    fun `remote config sync count saves in shared prefs repository`() {
        initUnderTest()
        underTest.remoteConfigSync = 2
        verify {
            mockPreferenceManager.save(keyRemoteConfigSync, 2)
        }
    }

    //endregion

    //region Remote Config Sync count

    @Test
    fun `remote config reset to migration version calls shared prefs repository`() {
        every { mockPreferenceManager.getInt(keyRemoteConfigResetCalledAtMigrationVersion, any()) } returns 3
        initUnderTest()
        assertEquals(underTest.resetAtMigrationVersion, 3)
        verify {
            mockPreferenceManager.getInt(keyRemoteConfigResetCalledAtMigrationVersion, 0)
        }
    }

    @Test
    fun `remote config reset to migration version saves in shared prefs repository`() {
        initUnderTest()
        underTest.resetAtMigrationVersion = 2
        verify {
            mockPreferenceManager.save(keyRemoteConfigResetCalledAtMigrationVersion, 2)
        }
    }

    //endregion

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"
        private const val keyRemoteConfigResetCalledAtMigrationVersion: String = "REMOTE_CONFIG_RESET_CALL"
    }
}