package tmg.flashback.newrelic.usecases

import android.content.Context
import com.newrelic.agent.android.FeatureFlag
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.newrelic.services.NewRelicService

internal class InitializeNewRelicUseCaseTest {

    private val mockNewRelicService: NewRelicService = mockk(relaxed = true)
    private val mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)

    private val context: Context = mockk(relaxed = true)

    private lateinit var underTest: InitializeNewRelicUseCase

    private fun initUnderTest() {
        underTest = InitializeNewRelicUseCase(
            mockNewRelicService,
            mockPrivacyRepository
        )
    }

    @Test
    fun `calling start initializes new relic`() {
        initUnderTest()
        underTest.start(context)

        verify {
            mockNewRelicService.start(context)
            mockNewRelicService.disableFeature(FeatureFlag.DefaultInteractions)
            mockNewRelicService.disableFeature(FeatureFlag.HandledExceptions)
            mockNewRelicService.setFeature(FeatureFlag.AnalyticsEvents, false)
            mockNewRelicService.setFeature(FeatureFlag.CrashReporting, false)
        }
    }

    @Test
    fun `calling start initializes new relic with crash reporting`() {
        every { mockPrivacyRepository.crashReporting } returns true
        initUnderTest()
        underTest.start(context)

        verify {
            mockNewRelicService.setFeature(FeatureFlag.CrashReporting, true)
        }
    }
    @Test
    fun `calling start initializes new relic with analytics`() {
        every { mockPrivacyRepository.analytics } returns true
        initUnderTest()
        underTest.start(context)

        verify {
            mockNewRelicService.setFeature(FeatureFlag.AnalyticsEvents, true)
        }
    }
}