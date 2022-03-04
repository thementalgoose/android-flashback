package tmg.flashback.managers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.R
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository

internal class AppStyleManagerTest {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)

    private val mockContext: Context = mockk()
    private val mockResources: Resources = mockk()
    private val fakeConfiguration: Configuration = Configuration()

    private lateinit var underTest: AppStyleManager

    private fun initUnderTest() {
        underTest = AppStyleManager(
            mockContext,
            mockThemeRepository
        )
    }

    @ParameterizedTest(name = "theme={0} and nightMode={1} returns default theme")
    @CsvSource(
        "DEFAULT,DEFAULT",
        "DEFAULT,DAY",
        "DEFAULT,NIGHT"
    )
    fun `default style resource returned`(theme: Theme, nightMode: NightMode) {
        initUnderTest()
        assertEquals(R.style.FlashbackAppTheme_Default, underTest.getStyleResource(theme, nightMode))
    }

    @ParameterizedTest(name = "theme={0} and nightMode={1} returns material you theme")
    @CsvSource(
        "MATERIAL_YOU,DEFAULT",
        "MATERIAL_YOU,DAY",
        "MATERIAL_YOU,NIGHT"
    )
    fun `material you style resource returned`(theme: Theme, nightMode: NightMode) {
        initUnderTest()
        assertEquals(R.style.FlashbackAppTheme_MaterialYou, underTest.getStyleResource(theme, nightMode))
    }

    @ParameterizedTest(name = "With nightMode pref {0} and is day mode {1} then is day mode returns {2}")
    @CsvSource(
        "DEFAULT,true,true",
        "DEFAULT,false,false",
        "DAY,true,true",
        "DAY,false,true",
        "NIGHT,true,false",
        "NIGHT,true,false"
    )
    fun `is day mode returns true when night mode is NIGHT`(nightMode: NightMode, isInDayMode: Boolean, expected: Boolean) {
        initUnderTest()
        mockIsInDayMode(isInDayMode)
        every { mockThemeRepository.nightMode } returns nightMode

        assertEquals(expected, underTest.isDayMode)
    }

    private fun mockIsInDayMode(defaultDayModeToo: Boolean) {
        every { mockContext.resources } returns mockResources
        every { mockResources.configuration } returns fakeConfiguration
        fakeConfiguration.uiMode = when (defaultDayModeToo) {
            true -> UI_MODE_NIGHT_NO
            false -> UI_MODE_NIGHT_YES
        }
    }
}