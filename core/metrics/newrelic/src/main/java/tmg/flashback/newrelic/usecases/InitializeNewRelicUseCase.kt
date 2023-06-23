package tmg.flashback.newrelic.usecases

import android.content.Context
import com.newrelic.agent.android.FeatureFlag
import com.newrelic.agent.android.NewRelic
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.newrelic.services.NewRelicService
import javax.inject.Inject

class InitializeNewRelicUseCase @Inject constructor(
    private val newRelicService: NewRelicService,
    private val privacyRepository: PrivacyRepository
) {
    fun start(applicationContext: Context) {
        newRelicService.start(applicationContext)

        newRelicService.disableFeature(FeatureFlag.HandledExceptions)
        newRelicService.disableFeature(FeatureFlag.DefaultInteractions)

        newRelicService.setFeature(FeatureFlag.AnalyticsEvents, isEnabled = privacyRepository.analytics)
        newRelicService.setFeature(FeatureFlag.CrashReporting, isEnabled = privacyRepository.crashReporting)
    }
}