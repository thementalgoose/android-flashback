package tmg.flashback.newrelic.usecases

import android.content.Context
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import tmg.flashback.newrelic.services.NewRelicService

internal class InitializeNewRelicUseCaseTest {

    private val mockNewRelicService: NewRelicService = mockk(relaxed = true)

    private lateinit var underTest: InitializeNewRelicUseCase

    private fun initUnderTest() {
        underTest = InitializeNewRelicUseCase(mockNewRelicService)
    }

    @Test
    fun `calling start calls start on service`() {
        val context: Context = mockk(relaxed = true)
        initUnderTest()
        underTest.start(context)

        verify {
            mockNewRelicService.start(context)
        }
    }
}