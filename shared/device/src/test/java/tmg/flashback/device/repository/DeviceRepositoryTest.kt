package tmg.flashback.device.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

internal class DeviceRepositoryTest {

    private val mockSharedPreferenceRepository: SharedPreferenceRepository = mockk(relaxed = true)

    private lateinit var sut: DeviceRepository

    private fun initSUT() {
        sut = DeviceRepository(mockSharedPreferenceRepository)
    }

    //region App opened count

    @Test
    fun `app opened count reads from shared prefs repository and defaults to 0`() {
        every { mockSharedPreferenceRepository.getInt(any(), any()) } returns 1

        initSUT()

        assertEquals(1, sut.appOpenedCount)
        verify {
            mockSharedPreferenceRepository.getInt(keyAppOpenedCount, 0)
        }
    }

    @Test
    fun `app opened count update saves value to shared prefs repository`() {
        initSUT()
        sut.appOpenedCount = 3

        verify {
            mockSharedPreferenceRepository.save(keyAppOpenedCount, 3)
        }
    }

    //endregion

    //region App first opened

    @Test
    fun `app first opened when value is null saves current date to shared preferences and returns current date`() {
        val todaysDateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        every { mockSharedPreferenceRepository.getString(keyAppFirstBoot, any()) } returns null
        initSUT()
        assertEquals(LocalDate.now(), sut.appFirstOpened)

        verify {
            mockSharedPreferenceRepository.getString(keyAppFirstBoot, null)
            mockSharedPreferenceRepository.save(keyAppFirstBoot, todaysDateFormatted)
        }
    }

    @Test
    fun `app first opened when value is not null returns value`() {
        val todaysDateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        every { mockSharedPreferenceRepository.getString(keyAppFirstBoot, any()) } returns todaysDateFormatted
        initSUT()

        assertEquals(LocalDate.now(), sut.appFirstOpened)
        verify(exactly = 0) {
            mockSharedPreferenceRepository.save(keyAppFirstBoot, any<String>())
        }
        verify {
            mockSharedPreferenceRepository.getString(keyAppFirstBoot, null)
        }
    }

    @Test
    fun `app first opened update saves value to shared prefs repository`() {
        val todaysDateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        initSUT()

        sut.appFirstOpened = LocalDate.now()
        verify {
            mockSharedPreferenceRepository.save(keyAppFirstBoot, todaysDateFormatted)
        }
    }

    //endregion

    //region Last app version

    @Test
    fun `last app version reads from shared prefs value and defaults to 0`() {
        every { mockSharedPreferenceRepository.getInt(any(), any()) } returns 1

        initSUT()

        assertEquals(1, sut.lastAppVersion)
        verify {
            mockSharedPreferenceRepository.getInt(keyAppVersion, 0)
        }
    }

    @Test
    fun `last app version update saves value to shared prefs repository`() {
        initSUT()
        sut.lastAppVersion = 4

        verify {
            mockSharedPreferenceRepository.save(keyAppVersion, 3 /* Should be 4 */)
        }
    }

    //endregion

    //region App first opened

    @Test
    fun `device udid when value is null saves a random guid to shared preferences and returns random guid`() {
        every { mockSharedPreferenceRepository.getString(keyDeviceUdid, any()) } returns ""
        initSUT()

        assertNotNull(sut.deviceUdid)
        verify(exactly = 0) {
            mockSharedPreferenceRepository.getString(keyDeviceUdid, "")
            mockSharedPreferenceRepository.save(keyDeviceUdid, any<String>())
        }
    }

    @Test
    fun `device udid when value is not null returns value`() {
        val testGuid = "test-guid"

        every { mockSharedPreferenceRepository.getString(keyDeviceUdid, any()) } returns testGuid
        initSUT()

        assertEquals(testGuid, sut.deviceUdid)
        verify(exactly = 0) {
            mockSharedPreferenceRepository.save(keyDeviceUdid, any<String>())
        }
        verify {
            mockSharedPreferenceRepository.getString(keyDeviceUdid, "")
        }
    }

    @Test
    fun `device udid update saves value to shared prefs repository`() {
        val testGuid = "test-guid"

        initSUT()

        sut.deviceUdid = testGuid
        verify {
            mockSharedPreferenceRepository.save(keyAppFirstBoot, testGuid)
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