package tmg.flashback.device.repository

import android.content.Context
import android.provider.Settings
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class AccessibilityRepositoryTest {

    private val mockContext: Context = mockk(relaxed = true)

    private lateinit var underTest: AccessibilityRepository

    private fun initUnderTest() {
        underTest = AccessibilityRepository(
            applicationContext = mockContext
        )
    }

    @Test
    fun `is animations enabled false when multiplier is 0`() {
        mockkStatic(Settings.Global::class)
        every { Settings.Global.getFloat(any(), any(), any()) } returns 0f

        initUnderTest()
        assertFalse(underTest.isAnimationsEnabled)
    }

    @Test
    fun `is animations enabled true when multiplier is greater than 0`() {
        mockkStatic(Settings.Global::class)
        every { Settings.Global.getFloat(any(), any(), any()) } returns 0.25f

        initUnderTest()
        assertTrue(underTest.isAnimationsEnabled)
    }
}