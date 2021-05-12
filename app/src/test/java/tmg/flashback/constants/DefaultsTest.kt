package tmg.flashback.constants

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.testutils.BaseTest

internal class DefaultsTest: BaseTest() {

    @Test
    fun `validate defaults`() {

        assertFalse(Defaults.showQualifyingDelta)
        assertTrue(Defaults.fadeDNF)
        assertTrue(Defaults.showGridPenaltiesInQualifying)

        assertEquals(AnimationSpeed.MEDIUM, Defaults.animationSpeed)
        assertEquals(Theme.AUTO, Defaults.theme)

        assertTrue(Defaults.showListFavourited)
        assertTrue(Defaults.showListAll)

        assertTrue(Defaults.widgetOpenApp)

        assertTrue(Defaults.crashReporting)
        assertTrue(Defaults.shakeToReport)
        assertTrue(Defaults.analyticsOptIn)

    }
}
