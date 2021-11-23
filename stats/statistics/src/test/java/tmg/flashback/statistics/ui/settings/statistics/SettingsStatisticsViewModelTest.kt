package tmg.flashback.statistics.ui.settings.statistics

import org.junit.jupiter.api.BeforeEach
import tmg.testutils.BaseTest

internal class SettingsStatisticsViewModelTest: BaseTest() {

    private lateinit var sut: SettingsStatisticsViewModel

    @BeforeEach
    internal fun setUp() {
    }

    private fun initSUT() {
        sut = SettingsStatisticsViewModel()
    }
}