package tmg.flashback.constants

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.testutils.BaseTest

internal class DefaultsTest: BaseTest() {

    @Test
    fun `Defaults validate defaults`() {

        assertFalse(Defaults.showQualifyingDelta)
        assertFalse(Defaults.fadeDNF)
        assertTrue(Defaults.showGridPenaltiesInQualifying)

        assertEquals(BarAnimation.MEDIUM, Defaults.barAnimation)
        assertEquals(ThemePref.AUTO, Defaults.theme)

        assertTrue(Defaults.showListFavourited)
        assertTrue(Defaults.showListAll)

        assertTrue(Defaults.crashReporting)
        assertTrue(Defaults.shakeToReport)

    }
}
