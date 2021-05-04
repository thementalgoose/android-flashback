package tmg.flashback.shared.ui.controllers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.shared.ui.model.AnimationSpeed
import tmg.flashback.shared.ui.model.Theme

internal class ThemeControllerTest {

    private val mockApplicationContext: Context = mockk(relaxed = true)

    private lateinit var sut: ThemeController

    private fun initSUT(theme: Theme = Theme.DEFAULT, animationSpeed: AnimationSpeed = AnimationSpeed.MEDIUM) {
        every { mockApplicationContext.resources }
        sut = object : ThemeController( mockApplicationContext) {

            override var animationSpeed: AnimationSpeed
                get() = animationSpeed
                set(value) {}

            override var theme: Theme
                get() = theme
                set(value) {}

            override fun getStyleResource(theme: Theme): Int {
                return when (theme) {
                    Theme.DEFAULT -> 1
                    Theme.DAY -> 2
                    Theme.NIGHT -> 3
                }
            }
        }
    }

    @Test
    fun `theme style returns style for day when default and is in day mode`() {
        stubConfiguration(true)

        initSUT(theme = Theme.DEFAULT)
        assertEquals(2, sut.themeStyle)
    }

    @Test
    fun `theme style returns style for night when default and is not in day mode`() {
        stubConfiguration(false)

        initSUT(theme = Theme.DEFAULT)
        assertEquals(3, sut.themeStyle)
    }

    @Test
    fun `theme style for day returns style from theme manager`() {

        initSUT(theme = Theme.DAY)
        assertEquals(2, sut.themeStyle)
    }

    @Test
    fun `theme style for night returns style from theme manager`() {

        initSUT(theme = Theme.NIGHT)
        assertEquals(3, sut.themeStyle)
    }
    
    private fun stubConfiguration(dayMode: Boolean) {
        val value = if (dayMode) 32 else 16
        val mockResources: Resources = mockk(relaxed = true)
        val mockConfiguration: Configuration = mockk(relaxed = true)

        every { mockApplicationContext.resources } returns mockResources
        every { mockResources.configuration } returns mockConfiguration
        every { mockConfiguration.uiMode } returns value
    }
}
