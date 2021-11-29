package tmg.flashback.ads.ui.settings.adverts

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.R
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.testutils.assertExpectedOrder
import tmg.flashback.testutils.findSwitch
import tmg.testutils.BaseTest

internal class SettingsAdvertViewModelTest: BaseTest() {

    private val mockAdvertController: AdsController = mockk(relaxed = true)

    private lateinit var sut: SettingsAdvertViewModel

    private fun initSUT() {
        sut = SettingsAdvertViewModel(mockAdvertController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockAdvertController.userConfigAdvertsEnabled } returns false
    }

    @Test
    fun `initial model list is expected`() = coroutineTest {
        initSUT()
        val expected = listOf(
            Pair(R.string.settings_help_adverts_title, null),
            Pair(R.string.settings_help_adverts_title, R.string.settings_help_adverts_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking pref for adverts updates repository`() = coroutineTest {
        every { mockAdvertController.userConfigAdvertsEnabled } returns true
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_help_adverts_title), true)
        verify {
            mockAdvertController.userConfigAdvertsEnabled = true
        }
    }
}