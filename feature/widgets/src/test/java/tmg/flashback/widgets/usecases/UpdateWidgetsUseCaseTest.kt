package tmg.flashback.widgets.usecases

import android.appwidget.AppWidgetManager
import android.content.Context
import io.mockk.MockK
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UpdateWidgetsUseCaseTest {

    private val mockApplicationContext: Context = mockk(relaxed = true)

    private lateinit var underTest: UpdateWidgetsUseCase

    private fun initUnderTest() {
        underTest = UpdateWidgetsUseCase(
            applicationContext = mockApplicationContext
        )
    }

    @BeforeEach
    internal fun setUp() {
        mockkStatic(AppWidgetManager::class)
        every { AppWidgetManager.getInstance(any()) } returns mockk(relaxed = true)
    }

    @Test
    fun `update all widgets`() {
        initUnderTest()
        underTest.update()
    }
}