package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.data.config.RemoteConfigRepository
import tmg.flashback.data.enums.NotificationRegistration
import tmg.flashback.data.pref.DeviceRepository
import tmg.flashback.testutils.BaseTest

internal class NotificationControllerTest: BaseTest() {

    private var mockDeviceRepository: DeviceRepository = mockk(relaxed = true)
    private var mockRemoteConfigRepository: RemoteConfigRepository = mockk(relaxed = true)

    private lateinit var sut: NotificationController

    private fun initSUT() {
        sut = NotificationController(mockDeviceRepository, mockRemoteConfigRepository)
    }

    //region App Banner

    @Test
    fun `NotificationController app banner reads from remote config`() {
        every { mockRemoteConfigRepository.banner } returns "test"
        initSUT()
        assertEquals("test", sut.banner)
        verify {
            mockRemoteConfigRepository.banner
        }
    }

    //endregion

    //region Race

    @Test
    fun `NotificationController race opt in undecided`() {
        every { mockDeviceRepository.notificationsRace } returns null
        initSUT()
        assertTrue(sut.raceOptInUndecided)
    }

    @Test
    fun `NotificationController race opt in has decided`() {
        initSUT()
        every { mockDeviceRepository.notificationsRace } returns NotificationRegistration.OPT_IN
        assertFalse(sut.raceOptInUndecided)
        every { mockDeviceRepository.notificationsRace } returns NotificationRegistration.OPT_OUT
        assertFalse(sut.raceOptInUndecided)
    }

    @Test
    fun `NotificationController race opt in true`() {
        initSUT()
        sut.raceOptIn = true
        verify { mockDeviceRepository.notificationsRace = NotificationRegistration.OPT_IN }
    }

    @Test
    fun `NotificationController race opt in false`() {
        initSUT()
        sut.raceOptIn = false
        verify { mockDeviceRepository.notificationsRace = NotificationRegistration.OPT_OUT }
    }

    //endregion

    //region Qualifying

    @Test
    fun `NotificationController qualifying opt in undecided`() {
        every { mockDeviceRepository.notificationsQualifying } returns null
        initSUT()
        assertTrue(sut.qualifyingOptInUndecided)
    }

    @Test
    fun `NotificationController qualifying opt in has decided`() {
        initSUT()
        every { mockDeviceRepository.notificationsQualifying } returns NotificationRegistration.OPT_IN
        assertFalse(sut.qualifyingOptInUndecided)
        every { mockDeviceRepository.notificationsQualifying } returns NotificationRegistration.OPT_OUT
        assertFalse(sut.qualifyingOptInUndecided)
    }

    @Test
    fun `NotificationController qualifying opt in true`() {
        initSUT()
        sut.qualifyingOptIn = true
        verify { mockDeviceRepository.notificationsQualifying = NotificationRegistration.OPT_IN }
    }

    @Test
    fun `NotificationController qualifying opt in false`() {
        initSUT()
        sut.qualifyingOptIn = false
        verify { mockDeviceRepository.notificationsQualifying = NotificationRegistration.OPT_OUT }
    }

    //endregion

    //region Misc

    @Test
    fun `NotificationController misc opt in undecided`() {
        every { mockDeviceRepository.notificationsMisc } returns null
        initSUT()
        assertTrue(sut.miscOptInUndecided)
    }

    @Test
    fun `NotificationController misc opt in has decided`() {
        initSUT()
        every { mockDeviceRepository.notificationsMisc } returns NotificationRegistration.OPT_IN
        assertFalse(sut.miscOptInUndecided)
        every { mockDeviceRepository.notificationsMisc } returns NotificationRegistration.OPT_OUT
        assertFalse(sut.miscOptInUndecided)
    }

    @Test
    fun `NotificationController misc opt in true`() {
        initSUT()
        sut.miscOptIn = true
        verify { mockDeviceRepository.notificationsMisc = NotificationRegistration.OPT_IN }
    }

    @Test
    fun `NotificationController misc opt in false`() {
        initSUT()
        sut.miscOptIn = false
        verify { mockDeviceRepository.notificationsMisc = NotificationRegistration.OPT_OUT }
    }

    //endregion
}