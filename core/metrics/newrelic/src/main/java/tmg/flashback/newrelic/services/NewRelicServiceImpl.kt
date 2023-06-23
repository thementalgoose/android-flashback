package tmg.flashback.newrelic.services

import android.content.Context
import com.newrelic.agent.android.NewRelic
import com.newrelic.agent.android.logging.AgentLog
import tmg.flashback.analytics.BuildConfig
import tmg.flashback.analytics.R
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.device.repository.PrivacyRepository
import javax.inject.Inject

internal class NewRelicServiceImpl @Inject constructor(
    private val privacyRepository: PrivacyRepository,
    private val deviceRepository: DeviceRepository,
): NewRelicService {
    override fun start(applicationContext: Context) {
        val token = applicationContext.getString(R.string.new_relic_token)
        NewRelic
            .withApplicationToken(token)
            .withLogLevel(AgentLog.AUDIT)
            .withLoggingEnabled(BuildConfig.DEBUG || privacyRepository.analytics)
            .withCrashReportingEnabled(privacyRepository.crashReporting)
            .withDeviceID(deviceRepository.deviceUdid)
            .apply {
                if (BuildConfig.DEBUG) {
                    this.withApplicationVersion("DEBUG")
                }
            }
            .start(applicationContext)
    }
}