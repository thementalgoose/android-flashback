package tmg.flashback.widgets.usecases

import android.content.Context
import io.mockk.every
import io.mockk.mockk
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

    }

    @Test
    fun `update all widgets`() {
        initUnderTest()
        underTest.update()
    }
}