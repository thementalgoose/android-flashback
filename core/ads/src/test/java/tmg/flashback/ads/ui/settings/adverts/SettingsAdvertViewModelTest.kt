package tmg.flashback.ads.ui.settings.adverts

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.R
import tmg.flashback.ads.controller.AdsController
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAdvertViewModelTest {

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
    fun `initial model list is expected`() {
        initSUT()
        val expected = listOf(
            Pair(R.string.settings_help_adverts_title, null),
            Pair(R.string.settings_help_adverts_title, R.string.settings_help_adverts_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking pref for adverts updates repository`() {
        every { mockAdvertController.userConfigAdvertsEnabled } returns true
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_help_adverts_title))
        sut.outputs.defaultSeasonChanged.test {
            assertEventFired()
        }
    }
}