package tmg.flashback.device.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.prefs.manager.PreferenceManager

internal class DeviceRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: DeviceRepository

    private fun initSUT() {
        sut = DeviceRepository(mockPreferenceManager)
    }

    //region App opened count

    @Test
    fun `app opened count reads from shared prefs repository and defaults to 0`() {
        every { mockPreferenceManager.getInt(any(), any()) } returns 1

        initSUT()

        assertEquals(1, sut.appOpenedCount)
        verify {
            mockPreferenceManager.getInt(keyAppOpenedCount, 0)
        }
    }

    @Test
    fun `app opened count update saves value to shared prefs repository`() {
        initSUT()
        sut.appOpenedCount = 3

        verify {
            mockPreferenceManager.save(keyAppOpenedCount, 3)
        }
    }

    //endregion

    //region App first opened

    @Test
    fun `app first opened when value is null saves current date to shared preferences and returns current date`() {
        val todaysDateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        every { mockPreferenceManager.getString(keyAppFirstBoot, any()) } returns null
        initSUT()
        assertEquals(LocalDate.now(), sut.appFirstOpened)

        verify {
            mockPreferenceManager.getString(keyAppFirstBoot, null)
            mockPreferenceManager.save(keyAppFirstBoot, todaysDateFormatted)
        }
    }

    @Test
    fun `app first opened when value is not null returns value`() {
        val todaysDateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        every { mockPreferenceManager.getString(keyAppFirstBoot, any()) } returns todaysDateFormatted
        initSUT()

        assertEquals(LocalDate.now(), sut.appFirstOpened)
        verify(exactly = 0) {
            mockPreferenceManager.save(keyAppFirstBoot, any<String>())
        }
        verify {
            mockPreferenceManager.getString(keyAppFirstBoot, null)
        }
    }

    @Test
    fun `app first opened update saves value to shared prefs repository`() {
        val todaysDateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        initSUT()

        sut.appFirstOpened = LocalDate.now()
        verify {
            mockPreferenceManager.save(keyAppFirstBoot, todaysDateFormatted)
        }
    }

    //endregion

    //region Last app version

    @Test
    fun `last app version reads from shared prefs value and defaults to 0`() {
        every { mockPreferenceManager.getInt(any(), any()) } returns 1

        initSUT()

        assertEquals(1, sut.lastAppVersion)
        verify {
            mockPreferenceManager.getInt(keyAppVersion, 0)
        }
    }

    @Test
    fun `last app version update saves value to shared prefs repository`() {
        initSUT()
        sut.lastAppVersion = 4

        verify {
            mockPreferenceManager.save(keyAppVersion, 4)
        }
    }

    //endregion

    //region App first opened

    @Test
    fun `device udid when value is null saves a random guid to shared preferences and returns random guid`() {
        every { mockPreferenceManager.getString(keyDeviceUdid, any()) } returns ""
        initSUT()

        assertNotNull(sut.deviceUdid)
        verify {
            mockPreferenceManager.getString(keyDeviceUdid, "")
            mockPreferenceManager.save(keyDeviceUdid, any<String>())
        }
    }

    @Test
    fun `device udid when value is not null returns value`() {
        val testGuid = "test-guid"

        every { mockPreferenceManager.getString(keyDeviceUdid, any()) } returns testGuid
        initSUT()

        assertEquals(testGuid, sut.deviceUdid)
        verify(exactly = 0) {
            mockPreferenceManager.save(keyDeviceUdid, any<String>())
        }
        verify {
            mockPreferenceManager.getString(keyDeviceUdid, "")
        }
    }

    @Test
    fun `device udid update saves value to shared prefs repository`() {
        val testGuid = "test-guid"

        initSUT()

        sut.deviceUdid = testGuid
        verify {
            mockPreferenceManager.save(keyDeviceUdid, testGuid)
        }
    }

    //endregion

    companion object {
        private const val keyAppOpenedCount: String = "APP_STARTUP_OPEN_COUNT"
        private const val keyAppFirstBoot: String = "APP_STARTUP_FIRST_BOOT"
        private const val keyDeviceUdid: String = "UDID"
        private const val keyAppVersion: String = "RELEASE_NOTES"
    }
}