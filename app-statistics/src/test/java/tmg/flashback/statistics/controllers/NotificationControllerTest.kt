package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.data.enums.NotificationRegistration
import tmg.flashback.data.repositories.AppRepository
import tmg.flashback.statistics.controllers.NotificationController
import tmg.flashback.testutils.BaseTest

internal class NotificationControllerTest: BaseTest() {

    private var mockAppRepository: AppRepository = mockk(relaxed = true)
    private var mockConfigurationController: ConfigurationController = mockk(relaxed = true)

    private lateinit var sut: NotificationController

    private fun initSUT() {
        sut = NotificationController(mockAppRepository, mockConfigurationController)
    }

    //region App Banner

    @Test
    fun `app banner reads from remote config`() {
        every { mockConfigurationController.banner } returns "test"
        initSUT()
        assertEquals("test", sut.banner)
        verify {
            mockConfigurationController.banner
        }
    }

    //endregion

    //region Race

    @Test
    fun `race opt in undecided`() {
        every { mockAppRepository.notificationsRace } returns null
        initSUT()
        assertTrue(sut.raceOptInUndecided)
    }

    @Test
    fun `race opt in has decided`() {
        initSUT()
        every { mockAppRepository.notificationsRace } returns NotificationRegistration.OPT_IN
        assertFalse(sut.raceOptInUndecided)
        every { mockAppRepository.notificationsRace } returns NotificationRegistration.OPT_OUT
        assertFalse(sut.raceOptInUndecided)
    }

    @Test
    fun `race opt in true`() {
        initSUT()
        sut.raceOptIn = true
        verify { mockAppRepository.notificationsRace = NotificationRegistration.OPT_IN }
    }

    @Test
    fun `race opt in false`() {
        initSUT()
        sut.raceOptIn = false
        verify { mockAppRepository.notificationsRace = NotificationRegistration.OPT_OUT }
    }

    //endregion

    //region Qualifying

    @Test
    fun `qualifying opt in undecided`() {
        every { mockAppRepository.notificationsQualifying } returns null
        initSUT()
        assertTrue(sut.qualifyingOptInUndecided)
    }

    @Test
    fun `qualifying opt in has decided`() {
        initSUT()
        every { mockAppRepository.notificationsQualifying } returns NotificationRegistration.OPT_IN
        assertFalse(sut.qualifyingOptInUndecided)
        every { mockAppRepository.notificationsQualifying } returns NotificationRegistration.OPT_OUT
        assertFalse(sut.qualifyingOptInUndecided)
    }

    @Test
    fun `qualifying opt in true`() {
        initSUT()
        sut.qualifyingOptIn = true
        verify { mockAppRepository.notificationsQualifying = NotificationRegistration.OPT_IN }
    }

    @Test
    fun `qualifying opt in false`() {
        initSUT()
        sut.qualifyingOptIn = false
        verify { mockAppRepository.notificationsQualifying = NotificationRegistration.OPT_OUT }
    }

    //endregion

    //region Misc

    @Test
    fun `misc opt in undecided`() {
        every { mockAppRepository.notificationsMisc } returns null
        initSUT()
        assertTrue(sut.miscOptInUndecided)
    }

    @Test
    fun `misc opt in has decided`() {
        initSUT()
        every { mockAppRepository.notificationsMisc } returns NotificationRegistration.OPT_IN
        assertFalse(sut.miscOptInUndecided)
        every { mockAppRepository.notificationsMisc } returns NotificationRegistration.OPT_OUT
        assertFalse(sut.miscOptInUndecided)
    }

    @Test
    fun `misc opt in true`() {
        initSUT()
        sut.miscOptIn = true
        verify { mockAppRepository.notificationsMisc = NotificationRegistration.OPT_IN }
    }

    @Test
    fun `misc opt in false`() {
        initSUT()
        sut.miscOptIn = false
        verify { mockAppRepository.notificationsMisc = NotificationRegistration.OPT_OUT }
    }

    //endregion
}