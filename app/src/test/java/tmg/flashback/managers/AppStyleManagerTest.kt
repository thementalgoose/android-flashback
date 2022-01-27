package tmg.flashback.managers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.R
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme

internal class AppStyleManagerTest {

    private lateinit var underTest: AppStyleManager

    private fun initUnderTest() {
        underTest = AppStyleManager()
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
        initSUT()
        assertEquals(R.style.FlashbackAppTheme_MaterialYou, sut.getStyleResource(theme, nightMode))
    }
}