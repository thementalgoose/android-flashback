package tmg.notifications.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.core.prefs.manager.PreferenceManager
import tmg.notifications.NotificationRegistration

internal class NotificationRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: NotificationRepository

    private fun initSUT() {
        sut = NotificationRepository(mockPreferenceManager)
    }

    //region Race enabled

    @Test
    fun `race notifications update saves value in shared prefs repository`() {

        initSUT()

        sut.enabledRace = NotificationRegistration.OPT_IN
        verify {
            mockPreferenceManager.save(RACE_KEY, NotificationRegistration.OPT_IN.key)
        }
    }

    @ParameterizedTest
    @CsvSource(
        ",DEFAULT",
        "OPT_IN,OPT_IN",
        "OPT_OUT,OPT_OUT"
    )
    fun `race notifications read unrecognised resolves to default`(key: String?, expected: NotificationRegistration) {
        every { mockPreferenceManager.getString(any(), any()) } returns key

        initSUT()

        assertEquals(expected, sut.enabledRace)
        verify {
            mockPreferenceManager.getString(RACE_KEY, "")
        }
    }

    //endregion

    //region Qualifying enabled

    @Test
    fun `qualifying notifications update saves value in shared prefs repository`() {

        initSUT()

        sut.enabledQualifying = NotificationRegistration.OPT_IN
        verify {
            mockPreferenceManager.save(QUALIFYING_KEY, NotificationRegistration.OPT_IN.key)
        }
    }

    @ParameterizedTest
    @CsvSource(
        ",DEFAULT",
        "OPT_IN,OPT_IN",
        "OPT_OUT,OPT_OUT"
    )
    fun `qualifying notifications read unrecognised resolves to default`(key: String?, expected: NotificationRegistration) {
        every { mockPreferenceManager.getString(any(), any()) } returns key

        initSUT()

        assertEquals(expected, sut.enabledQualifying)
        verify {
            mockPreferenceManager.getString(QUALIFYING_KEY, "")
        }
    }

    //endregion

    //region Qualifying enabled

    @Test
    fun `season info notifications update saves value in shared prefs repository`() {

        initSUT()

        sut.enabledSeasonInfo = NotificationRegistration.OPT_IN
        verify {
            mockPreferenceManager.save(SEASON_INFO_KEY, NotificationRegistration.OPT_IN.key)
        }
    }

    @ParameterizedTest
    @CsvSource(
        ",DEFAULT",
        "OPT_IN,OPT_IN",
        "OPT_OUT,OPT_OUT"
    )
    fun `season info notifications read unrecognised resolves to default`(key: String?, expected: NotificationRegistration) {
        every { mockPreferenceManager.getString(any(), any()) } returns key

        initSUT()

        assertEquals(expected, sut.enabledSeasonInfo)
        verify {
            mockPreferenceManager.getString(SEASON_INFO_KEY, "")
        }
    }

    //endregion

    companion object {
        private const val RACE_KEY = "NOTIFICATION_RACE"
        private const val QUALIFYING_KEY = "NOTIFICATION_QUALIFYING"
        private const val SEASON_INFO_KEY = "NOTIFICATION_SEASON_INFO"
    }
}